package com.cygnussource.AsteroidTrackerAPI.services.impl;

import com.cygnussource.AsteroidTrackerAPI.services.AsteroidTrackerService;
import com.cygnussource.AsteroidTrackerAPI.services.bean.EstimatedDiameterUnit;
import com.cygnussource.AsteroidTrackerAPI.services.bean.MissDistanceUnit;
import com.cygnussource.AsteroidTrackerAPI.services.bean.RelativeVelocityUnit;

public class DistancesUnits {

    private MissDistanceUnit missDistanceUnit = MissDistanceUnit.ASTRONOMICAL;
    private RelativeVelocityUnit relativeVelocityUnit = RelativeVelocityUnit.KILOMETERS_PER_SECOND;
    private EstimatedDiameterUnit estimatedDiameterUnit = EstimatedDiameterUnit.KILOMETERS;

    /**
     * Establece la unidad en la que se devolverá la información del diametro del objeto.
     *
     * @param estimatedDiameterUnit {@link EstimatedDiameterUnit} Unidad de distancia seleccionada. Por defecto {@link EstimatedDiameterUnit#KILOMETERS}.
     * @return {@link AsteroidTrackerService}
     */
    public DistancesUnits withEstimatedDiameterUnit(EstimatedDiameterUnit estimatedDiameterUnit) {
        this.estimatedDiameterUnit = estimatedDiameterUnit;
        return this;
    }

    /**
     * Establece la unidad en la que se devolverá la información de la distancia.
     *
     * @param missDistanceUnity {@link MissDistanceUnit} Unidad de distancia seleccionada. Por defecto {@link MissDistanceUnit#ASTRONOMICAL}.
     * @return {@link AsteroidTrackerService}
     */
    public DistancesUnits withMissDistanceUnit(MissDistanceUnit missDistanceUnity) {
        this.missDistanceUnit = missDistanceUnity;
        return this;
    }

    /**
     * Establece la unidad en la que se devolverá la información sobre la velocidad relativa del objeto.
     *
     * @param relativeVelocityUnit {@link RelativeVelocityUnit} Unidad de velocidad relativa seleccionada. Por defecto {@link RelativeVelocityUnit#KILOMETERS_PER_SECOND}.
     * @return {@link AsteroidTrackerService}
     */
    public DistancesUnits withRelativeVelocityUnit(RelativeVelocityUnit relativeVelocityUnit) {
        this.relativeVelocityUnit = relativeVelocityUnit;
        return this;
    }


}
