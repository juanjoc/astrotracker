package com.cygnussource.AsteroidTrackerAPI.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class NearEarthObjectContent {

    @SerializedName( "element_count" )
    private int elementCount;
    @SerializedName( "near_earth_objects" )
    private Map<String, List<NearEarthObject>> nearEarthObjects;
//    private Map<String, String> nearEarthObjects;

}
