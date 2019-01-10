package com.cygnussource.AsteroidTrackerAPI.services.bean;

import lombok.Getter;

import java.util.Arrays;

public enum RelativeVelocityUnit {

    KILOMETERS_PER_SECOND("kilometers_per_second"), KILOMETERS_PER_HOUR("kilometers_per_hour"), MILES_PER_HOUR("miles_per_hour");

    @Getter
    private String unit;

    RelativeVelocityUnit(String unit) {
        this.unit = unit;
    }

    public RelativeVelocityUnit find(String val) {
        return Arrays.stream(values())
                .filter(e -> e.unit.equals(val))
                .findFirst()
                .orElse( KILOMETERS_PER_SECOND );
    }
}
