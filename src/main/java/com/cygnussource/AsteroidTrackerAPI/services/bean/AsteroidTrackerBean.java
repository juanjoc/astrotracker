package com.cygnussource.AsteroidTrackerAPI.services.bean;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.springframework.hateoas.ResourceSupport;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JsonDeserialize(builder = AsteroidTrackerBean.Builder.class)
@Getter
@EqualsAndHashCode(callSuper = false)
public class AsteroidTrackerBean extends ResourceSupport {


    private final String name;
    private final String neoReferenceID;
    private final Boolean potentiallyHazardous;
    private final Double absoluteMagnitude;
    private final Double estimatedDiameterMin;
    private final Double estimatedDiameterMax;

    private final List<ApproxData> approxData;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String missDistanceUnit;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String relativeVelocityUnit;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String estimatedDiameterUnit;


    @JsonCreator
    public AsteroidTrackerBean ( Builder builder ) {
        this.name = builder.id;
        this.neoReferenceID = builder.neoReferenceID;
        this.potentiallyHazardous = builder.potentiallyHazardous;
        this.absoluteMagnitude = builder.absoluteMagnitude;
        this.estimatedDiameterMin = builder.estimatedDiameterMin;
        this.estimatedDiameterMax = builder.estimatedDiameterMax;
        builder.missDistanceUnit.ifPresent(c -> missDistanceUnit = c.getUnit());
        builder.relativeVelocityUnit.ifPresent(c -> relativeVelocityUnit = c.getUnit());
        builder.estimatedDiameterUnit.ifPresent(c -> estimatedDiameterUnit = c.getUnit());
//        this.missDistanceUnit = builder.missDistanceUnit.getUnit();
//        this.relativeVelocityUnit = builder.relativeVelocityUnit.getUnit();
//        this.estimatedDiameterUnit = builder.estimatedDiameterUnit.getUnit();
        this.approxData = builder.approxData;
    }

    public static AsteroidTrackerBean VOID () {
        return new Builder().build();
    }

    @lombok.Builder
    @Getter
    public static class ApproxData {
        private final LocalDate closeApproachingDate;
        private final Double missDistance;
        private final Double relativeVelocity;
    }

    public static class Builder {

        private String id;
        private String neoReferenceID;
        private Boolean potentiallyHazardous;
        private Double absoluteMagnitude;
        private Double estimatedDiameterMin;
        private Double estimatedDiameterMax;

        private Optional<MissDistanceUnit> missDistanceUnit = Optional.empty();//of(MissDistanceUnit.ASTRONOMICAL);
        private Optional<RelativeVelocityUnit> relativeVelocityUnit = Optional.empty();//.of(RelativeVelocityUnit.KILOMETERS_PER_SECOND);
        private Optional<EstimatedDiameterUnit> estimatedDiameterUnit = Optional.empty();//.of(EstimatedDiameterUnit.KILOMETERS);

        private List<ApproxData> approxData = new ArrayList<>();

        public Builder withApproxData (LocalDate closeApproachingDate, Double missDistance, Double relativeVelocity) {
            this.approxData.add( ApproxData.builder().closeApproachingDate(closeApproachingDate).missDistance(missDistance).relativeVelocity(relativeVelocity).build() );
            return this;
        }

        public Builder withEstimatedDiameterUnit ( EstimatedDiameterUnit estimatedDiameterUnit) {
            this.estimatedDiameterUnit = Optional.ofNullable(estimatedDiameterUnit);
            return this;
        }

        public Builder withMissDistanceUnity ( MissDistanceUnit missDistanceUnity ) {
            this.missDistanceUnit = Optional.ofNullable(missDistanceUnity);
            return this;
        }

        public Builder withRelativeVelocityUnity ( RelativeVelocityUnit relativeVelocityUnit) {
            this.relativeVelocityUnit = Optional.ofNullable(relativeVelocityUnit);
            return this;
        }

        public Builder withId ( String id, String name ) {
            this.id = String.format("%s %s", id, name);
            return this;
        }

        public Builder withNeoReferenceID ( String neoReferenceID ) {
            this.neoReferenceID = neoReferenceID;
            return this;
        }

        public Builder withPotentiallyHazardous ( Boolean potentiallyHazardous ) {
            this.potentiallyHazardous = potentiallyHazardous;
            return this;
        }

        public Builder withAbsoluteMagnitude ( Double absoluteMagnitude ) {
            this.absoluteMagnitude = absoluteMagnitude;
            return this;
        }

        public Builder withEstimatedDiameterMin ( Double estimatedDiameterMin ) {
            this.estimatedDiameterMin = estimatedDiameterMin;
            return this;
        }

        public Builder withEstimatedDiameterMax ( Double estimatedDiameterMax ) {
            this.estimatedDiameterMax = estimatedDiameterMax;
            return this;
        }

        public AsteroidTrackerBean build () {
            return new AsteroidTrackerBean(this );
        }

    }

}
