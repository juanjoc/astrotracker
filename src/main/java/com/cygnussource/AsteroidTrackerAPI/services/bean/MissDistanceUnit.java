package com.cygnussource.AsteroidTrackerAPI.services.bean;

import lombok.Getter;

import java.util.Arrays;

public enum MissDistanceUnit  {

    ASTRONOMICAL("astronomical"), LUNAR("lunar"), KILOMETERS("kilometers"), MILES("miles");

    @Getter
    private String unit;

    MissDistanceUnit(String unity) {
        this.unit = unity;
    }

    public MissDistanceUnit find(String val) throws IllegalStateException {
        return Arrays.stream(MissDistanceUnit.values())
                .filter(e -> e.unit.equals(val))
                .findFirst()
                .orElse( ASTRONOMICAL );
    }

}
