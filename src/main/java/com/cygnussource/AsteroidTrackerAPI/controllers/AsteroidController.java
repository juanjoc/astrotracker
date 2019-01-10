package com.cygnussource.AsteroidTrackerAPI.controllers;

import com.cygnussource.AsteroidTrackerAPI.exceptions.AsteroidNotFoundException;
import com.cygnussource.AsteroidTrackerAPI.exceptions.AsteroidTrackerException;
import com.cygnussource.AsteroidTrackerAPI.exceptions.DateFormatException;
import com.cygnussource.AsteroidTrackerAPI.exceptions.NASANotRespondsException;
import com.cygnussource.AsteroidTrackerAPI.services.AsteroidTrackerService;
import com.cygnussource.AsteroidTrackerAPI.services.bean.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
public class AsteroidController {


    private static Logger logger = Logger.getLogger(AsteroidController.class.getName());

    static DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").parseStrict().toFormatter();

    private final static String MISS_DISTANCE_UNIT_DEFAULT = "astronomical";
    private final static String RELATIVE_VELOCITY_UNIT_DEFAULT = "kilometers_per_second";
    private final static String ESTIMATED_DIAMTER_UNIT = "kilometers";

    @Autowired
    private AsteroidTrackerService asteroidTrackerService;

    @Autowired
    private Environment environment;

    @Autowired
    private EntityLinks entityLinks;

    @RequestMapping( value = "/asteroids", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE} )
    public AsteroidTrackerContent getByDate (@RequestParam(required = false, name = "start_date") String  fromDate,
                                             @RequestParam(required = false, name = "end_date") String toDate,
                                             @RequestParam(required = false, name = "missDistanceUnit", defaultValue = MISS_DISTANCE_UNIT_DEFAULT ) String missDistanceUnit,
                                             @RequestParam(required = false, name = "relativeVelocityUnit", defaultValue = RELATIVE_VELOCITY_UNIT_DEFAULT) String relativeVelocityUnit,
                                             @RequestParam(required = false, name = "estimatedDiameterUnit", defaultValue = ESTIMATED_DIAMTER_UNIT) String estimatedDiameterUnit) {

        LocalDate startDate;
        LocalDate endDate;
        LocalDate sevenPlusDays = LocalDate.now().plusDays(environment.getProperty("nasa.maxDaysInQuery", Integer.class));

        try {
            startDate = LocalDate.parse(fromDate, formatter);
            endDate = StringUtils.isBlank(toDate) ?
                    sevenPlusDays : LocalDate.parse(toDate, formatter).isAfter(startDate.plusDays(environment.getProperty("nasa.maxDaysInQuery", Integer.class))) ?
                            sevenPlusDays : LocalDate.parse(toDate, formatter);

            AsteroidTrackerContent resource = asteroidTrackerService
                    .withMissDistanceUnit(Arrays.stream(MissDistanceUnit.values()).map(f -> f.find(missDistanceUnit)).findFirst().get())
                    .withRelativeVelocityUnit(Arrays.stream(RelativeVelocityUnit.values()).map(m -> m.find(relativeVelocityUnit)).findFirst().get())
                    .withEstimatedDiameterUnit(Arrays.stream(EstimatedDiameterUnit.values()).map(m -> m.find(estimatedDiameterUnit)).findFirst().get())
                    .getAsteroidsByDate(startDate, endDate)
                    .get(5L, TimeUnit.SECONDS);

            resource.getAsteroidsObjects()
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().stream().map(m -> hateoas.apply(m) ).collect(Collectors.toList())));

            long numDaysInQuery = ChronoUnit.DAYS.between(startDate, endDate);

            Link previous = ControllerLinkBuilder.linkTo(
                    ControllerLinkBuilder.methodOn(AsteroidController.class)
                            .getByDate(startDate.minusDays(numDaysInQuery).format(formatter), startDate.format(formatter), missDistanceUnit, relativeVelocityUnit, estimatedDiameterUnit)
            ).withRel("prev");

            Link self = ControllerLinkBuilder.linkTo(
                    ControllerLinkBuilder.methodOn(AsteroidController.class)
                            .getByDate(startDate.format(formatter), endDate.format(formatter), missDistanceUnit, relativeVelocityUnit, estimatedDiameterUnit)
            ).withSelfRel();

            Link next = ControllerLinkBuilder.linkTo(
                    ControllerLinkBuilder.methodOn(AsteroidController.class)
                            .getByDate(endDate.format(formatter), endDate.plusDays(numDaysInQuery).format(formatter), missDistanceUnit, relativeVelocityUnit, estimatedDiameterUnit)
            ).withRel("next");

            resource.add(previous, self, next);

            return resource;

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            logger.log(Level.SEVERE, e, () -> String.format("Error: %s - %s", e.getCause(), e.getMessage()));
            throw  e.getCause() instanceof HttpClientErrorException.NotFound ?  new AsteroidNotFoundException( e.getMessage(), e ) :
                    e.getCause().getMessage().contains("UnknownHostException") || e.getCause() instanceof TimeoutException ? new NASANotRespondsException(e.getMessage()) : new AsteroidTrackerException(e.getMessage());

        } catch (DateTimeException e) {
            logger.log(Level.SEVERE, e, () -> String.format("Error: %s - %s", e.getCause(), e.getMessage()));
            throw new DateFormatException( e.getMessage(), e );
        }
    }

    @RequestMapping( value = "/asteroids/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE} )
    public HttpEntity<AsteroidTrackerBean> getById (@PathVariable("id") String id,
                                                   @RequestParam(required = false, name = "missDistanceUnit", defaultValue = MISS_DISTANCE_UNIT_DEFAULT ) String missDistanceUnit,
                                                   @RequestParam(required = false, name = "relativeVelocityUnit", defaultValue = RELATIVE_VELOCITY_UNIT_DEFAULT) String relativeVelocityUnit,
                                                   @RequestParam(required = false, name = "estimatedDiameterUnit", defaultValue = ESTIMATED_DIAMTER_UNIT) String estimatedDiameterUnit) {

        try {

            AsteroidTrackerBean resource = asteroidTrackerService
                    .withMissDistanceUnit(Arrays.stream(MissDistanceUnit.values()).map(f -> f.find(missDistanceUnit)).findFirst().get())
                    .withRelativeVelocityUnit(Arrays.stream(RelativeVelocityUnit.values()).map(m -> m.find(relativeVelocityUnit)).findFirst().get())
                    .withEstimatedDiameterUnit(Arrays.stream(EstimatedDiameterUnit.values()).map(m -> m.find(estimatedDiameterUnit)).findFirst().get())
                    .getAsteroidByID(id)
                    .get(5L, TimeUnit.SECONDS);

            return new ResponseEntity<>( hateoas.apply(resource), HttpStatus.OK );

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            logger.log(Level.SEVERE, e, () -> String.format("Error: %s - %s", e.getCause(), e.getMessage()));

//            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).header("Retry-After", "120").build();

            throw  e.getCause() instanceof HttpClientErrorException.NotFound ?  new AsteroidNotFoundException( e.getMessage(), e ) :
                    e.getCause().getMessage().contains("UnknownHostException") || e.getCause() instanceof TimeoutException ? new NASANotRespondsException(e.getMessage()) : new AsteroidTrackerException(e.getMessage());
        }

    }


    BiFunction<Unit[], String, Unit> unitSelector = (l, u) -> (Unit) Arrays.stream(l).map(m -> (m).find(u)).findFirst().get();

    UnaryOperator<AsteroidTrackerBean> hateoas = asteroidTrackerBean -> {
        asteroidTrackerBean.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder.methodOn(AsteroidController.class)
                                .getById(asteroidTrackerBean.getNeoReferenceID(),
                                        asteroidTrackerBean.getMissDistanceUnit(),
                                        asteroidTrackerBean.getRelativeVelocityUnit(),
                                        asteroidTrackerBean.getEstimatedDiameterUnit()
                                )
                )
                        .withSelfRel());
        return asteroidTrackerBean;
    };


}
