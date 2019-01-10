package com.cygnussource.AsteroidTrackerAPI.services.bean;

import com.cygnussource.AsteroidTrackerAPI.model.NearEarthObject;
import com.cygnussource.AsteroidTrackerAPI.model.NearEarthObjectContent;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AsteroidTrackerFactory {

    private static Logger logger = Logger.getLogger(AsteroidTrackerFactory.class.getName());

    public static AsteroidTrackerContent create (NearEarthObjectContent neo, MissDistanceUnit missDistanceUnit, EstimatedDiameterUnit estimatedDiameterUnit, RelativeVelocityUnit relativeVelocityUnit ) {

        Map<String, List<AsteroidTrackerBean>> collect = neo.getNearEarthObjects()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().stream().map(m -> AsteroidTrackerFactory.create(m, missDistanceUnit, estimatedDiameterUnit, relativeVelocityUnit)).collect(Collectors.toList())));

        return AsteroidTrackerContent.builder()
                .elementCount(collect.entrySet().stream().mapToInt(e -> e.getValue().size()).sum())
                .asteroidsObjects(collect)
                .missDistanceUnit(missDistanceUnit.getUnit())
                .estimatedDiameterUnit(estimatedDiameterUnit.getUnit())
                .relativeVelocityUnit(relativeVelocityUnit.getUnit())
                .build();

    }

    public static AsteroidTrackerBean create (NearEarthObject neo, MissDistanceUnit missDistanceUnit, EstimatedDiameterUnit estimatedDiameterUnit, RelativeVelocityUnit relativeVelocityUnit ) {

        AsteroidTrackerBean build = null;
        if (neo != null) {

            AsteroidTrackerBean.Builder builder = new AsteroidTrackerBean.Builder()
                    .withId(neo.getId(), neo.getName())
                    .withNeoReferenceID(neo.getNeoReferenceId())
                    .withAbsoluteMagnitude(neo.getAbsoluteMagnitudeH())
                    .withPotentiallyHazardous(neo.getPotentiallyHazardousAsteroid())
                    .withEstimatedDiameterMax(neo.getEstimatedDiameter().get(estimatedDiameterUnit.getUnit()).getMax())
                    .withEstimatedDiameterMin(neo.getEstimatedDiameter().get(estimatedDiameterUnit.getUnit()).getMin())
                    .withMissDistanceUnity(missDistanceUnit)
                    .withRelativeVelocityUnity(relativeVelocityUnit)
                    .withEstimatedDiameterUnit(estimatedDiameterUnit);

            neo.getCloseApproachData().stream().forEach( f -> builder.withApproxData( f.getCloseApproachDate(),
                                                                                      f.getMissDistance().get( missDistanceUnit.getUnit() ),
                                                                                      f.getRelativeVelocity().get( relativeVelocityUnit.getUnit() )));

            build = builder.build();

        }

        return build;
    }

}
