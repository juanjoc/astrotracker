package com.cygnussource.AsteroidTrackerAPI.services;

import com.cygnussource.AsteroidTrackerAPI.services.bean.*;
import com.cygnussource.AsteroidTrackerAPI.services.impl.DistancesUnits;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public interface AsteroidTrackerService {

    AsteroidTrackerService distancesUnit(UnaryOperator<DistancesUnits> units);


    /**
     * Establece la unidad en la que se devolverá la información del diametro del objeto.
     *
     * @param estimatedDiameterUnit {@link EstimatedDiameterUnit} Unidad de distancia seleccionada. Por defecto {@link EstimatedDiameterUnit#KILOMETERS}.
     *
     * @return {@link AsteroidTrackerService}
     */
    AsteroidTrackerService withEstimatedDiameterUnit(EstimatedDiameterUnit estimatedDiameterUnit);

    /**
     * Establece la unidad en la que se devolverá la información de la distancia.
     *
     * @param missDistanceUnity {@link MissDistanceUnit} Unidad de distancia seleccionada. Por defecto {@link MissDistanceUnit#ASTRONOMICAL}.
     *
     * @return {@link AsteroidTrackerService}
     */
    AsteroidTrackerService withMissDistanceUnit(MissDistanceUnit missDistanceUnity);

    /**
     * Establece la unidad en la que se devolverá la información sobre la velocidad relativa del objeto.
     *
     * @param relativeVelocityUnit {@link RelativeVelocityUnit} Unidad de velocidad relativa seleccionada. Por defecto {@link RelativeVelocityUnit#KILOMETERS_PER_SECOND}.
     *
     * @return {@link AsteroidTrackerService}
     */
    AsteroidTrackerService withRelativeVelocityUnit (RelativeVelocityUnit relativeVelocityUnit);

    /**
     * Recupera los datos de trayectoria de un asteroide por su ID.
     *
     * @param id {@link String} Identificador del asteroide.
     * @return {@link CompletableFuture} Devuelve un futuro de {@link AsteroidTrackerBean}.
     */
    CompletableFuture<AsteroidTrackerBean> getAsteroidByID(String id);

    /**
     * Recupera una lista de los asteroides más próximos entre las fechas indicadas.
     *
     * @param startDate {@link LocalDate} Fecha de inicio.
     * @param endDate {@link LocalDate} Fecha de fin.
     *
     * @return {@link CompletableFuture} Un futuro de {@link AsteroidTrackerContent} con los asteroides próxmios entre las fechas proporcionadas.
     */
    CompletableFuture<AsteroidTrackerContent> getAsteroidsByDate(LocalDate startDate, LocalDate endDate);

}
