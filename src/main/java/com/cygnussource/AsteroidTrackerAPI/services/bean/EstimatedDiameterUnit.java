package com.cygnussource.AsteroidTrackerAPI.services.bean;

import lombok.Getter;

import java.util.Arrays;

public enum EstimatedDiameterUnit {

    KILOMETERS("kilometers"), METERS("meters"), Miles("miles"), FEET("feet");

    @Getter
    private String unit;

    EstimatedDiameterUnit(String unit) {
        this.unit = unit;
    }

    public EstimatedDiameterUnit find(String val) {
        return Arrays.stream(EstimatedDiameterUnit.values())
                .filter(e -> e.unit.equals(val))
                .findFirst()
                .orElse( KILOMETERS );
    }
}
