package com.cygnussource.AsteroidTrackerAPI.controllers.filters;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;

@JsonDeserialize(builder = AsteroidQueryFilter.Builder.class)
public class AsteroidQueryFilter {

    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final String id;

    public AsteroidQueryFilter () {
        this.fromDate = null;
        this.toDate = null;
        this.id = null;
    }

    public AsteroidQueryFilter ( Builder builder ) {
        this.fromDate = builder.fromDate;
        this.toDate = builder.toDate;
        this.id = builder.id;
    }

    public static class Builder {

        private LocalDate fromDate;
        private LocalDate toDate;
        private String id;

        public Builder withFromDate ( LocalDate fromDate ) {
            this.fromDate = fromDate;
            return this;
        }

        public Builder withToDate ( LocalDate toDate ) {
            this.toDate = toDate;
            return this;
        }

        public Builder withId (String id) {
            this.id = id;
            return this;
        }

        public AsteroidQueryFilter build () {
            return new AsteroidQueryFilter(this);
        }


    }

}
