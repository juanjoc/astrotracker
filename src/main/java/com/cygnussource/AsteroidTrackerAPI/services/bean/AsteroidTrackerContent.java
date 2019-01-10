package com.cygnussource.AsteroidTrackerAPI.services.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class AsteroidTrackerContent extends ResourceSupport {

    private int elementCount;
    private Map<String, List<AsteroidTrackerBean>> asteroidsObjects;

    @JsonIgnore()
    private final String missDistanceUnit;
    @JsonIgnore()
    private final String relativeVelocityUnit;
    @JsonIgnore()
    private final String estimatedDiameterUnit;




}
