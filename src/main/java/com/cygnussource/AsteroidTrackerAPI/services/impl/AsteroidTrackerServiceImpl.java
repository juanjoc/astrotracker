package com.cygnussource.AsteroidTrackerAPI.services.impl;

import com.cygnussource.AsteroidTrackerAPI.clients.HttpClient;
import com.cygnussource.AsteroidTrackerAPI.model.LocalDateDeserializer;
import com.cygnussource.AsteroidTrackerAPI.model.NearEarthObject;
import com.cygnussource.AsteroidTrackerAPI.model.NearEarthObjectContent;
import com.cygnussource.AsteroidTrackerAPI.services.AsteroidTrackerService;
import com.cygnussource.AsteroidTrackerAPI.services.bean.*;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AsteroidTrackerServiceImpl implements AsteroidTrackerService {

    @Autowired
    private Environment environment;

    @Autowired
    private HttpClient httpClient;


    private static GsonBuilder gson;
    static {
        gson = new GsonBuilder().setDateFormat( "yyyy-MM-dd HH:mm:ss" );
        gson.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
    }


    private static Logger logger = Logger.getLogger(AsteroidTrackerServiceImpl.class.getName());

    private MissDistanceUnit missDistanceUnit = MissDistanceUnit.ASTRONOMICAL;
    private RelativeVelocityUnit relativeVelocityUnit = RelativeVelocityUnit.KILOMETERS_PER_SECOND;
    private EstimatedDiameterUnit estimatedDiameterUnit = EstimatedDiameterUnit.KILOMETERS;

    @Override
    public AsteroidTrackerService distancesUnit(UnaryOperator<DistancesUnits> units) {
        return null;
    }

    /**
     * Establece la unidad en la que se devolverá la información del diametro del objeto.
     *
     * @param estimatedDiameterUnit {@link EstimatedDiameterUnit} Unidad de distancia seleccionada. Por defecto {@link EstimatedDiameterUnit#KILOMETERS}.
     * @return {@link AsteroidTrackerService}
     */
    @Override
    public AsteroidTrackerService withEstimatedDiameterUnit(EstimatedDiameterUnit estimatedDiameterUnit) {
        this.estimatedDiameterUnit = estimatedDiameterUnit;
        return this;
    }

    /**
     * Establece la unidad en la que se devolverá la información de la distancia.
     *
     * @param missDistanceUnity {@link MissDistanceUnit} Unidad de distancia seleccionada. Por defecto {@link MissDistanceUnit#ASTRONOMICAL}.
     * @return {@link AsteroidTrackerService}
     */
    @Override
    public AsteroidTrackerService withMissDistanceUnit(MissDistanceUnit missDistanceUnity) {
        this.missDistanceUnit = missDistanceUnity;
        return this;
    }

    /**
     * Establece la unidad en la que se devolverá la información sobre la velocidad relativa del objeto.
     *
     * @param relativeVelocityUnit {@link RelativeVelocityUnit} Unidad de velocidad relativa seleccionada. Por defecto {@link RelativeVelocityUnit#KILOMETERS_PER_SECOND}.
     * @return {@link AsteroidTrackerService}
     */
    @Override
    public AsteroidTrackerService withRelativeVelocityUnit(RelativeVelocityUnit relativeVelocityUnit) {
        this.relativeVelocityUnit = relativeVelocityUnit;
        return this;
    }

    /**
     * Recupera los datos de trayectoria de un asteroide por su ID.
     *
     * @param id {@link String} Identificador del asteroide.
     * @return {@link CompletableFuture} Devuelve un futuro de {@link AsteroidTrackerBean}.
     */
    @Override
    public CompletableFuture<AsteroidTrackerBean> getAsteroidByID(String id) {


        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(environment.getProperty("nasa.scheme"))
                .host(environment.getProperty("nasa.server"))
                .path(environment.getProperty("nasa.neoLookup"))
                .path("/{id}")
                .queryParam("api_key", environment.getProperty("nasa.token"))
                .buildAndExpand( id );

        logger.log(Level.INFO, () -> String.format( "URI: %s", uriComponents.toUriString() ) );

        return httpClient.get(uriComponents.toUriString())
                .thenApply(s -> gson.create().fromJson(s, NearEarthObject.class))
                .thenApply(neo -> AsteroidTrackerFactory.create(neo, missDistanceUnit, estimatedDiameterUnit, relativeVelocityUnit));


    }

    /**
     * Recupera una lista de los asteroides más próximos entre las fechas indicadas.
     *
     * @param startDate {@link LocalDate} Fecha de inicio.
     * @param endDate   {@link LocalDate} Fecha de fin.
     * @return {@link CompletableFuture} Un futuro de {@link NearEarthObjectContent} con los asteroides próxmios entre las fechas proporcionadas.
     */
    @Override
    public CompletableFuture<AsteroidTrackerContent> getAsteroidsByDate(LocalDate startDate, LocalDate endDate) {

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(environment.getProperty("nasa.scheme"))
                .host(environment.getProperty("nasa.server"))
                .path(environment.getProperty("nasa.neoFeed"))
                .queryParam("start_date", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .queryParam("end_date", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .queryParam("api_key", environment.getProperty("nasa.token"))
                .build(true);

        logger.log(Level.INFO, () -> String.format( "URI: %s", uriComponents.toUriString() ) );

        return httpClient
                .get(uriComponents.toUriString())
                .thenApply(s -> gson.create().fromJson(s, NearEarthObjectContent.class))
                .thenApply(neo -> AsteroidTrackerFactory.create(neo, missDistanceUnit, estimatedDiameterUnit, relativeVelocityUnit));

    }
}
