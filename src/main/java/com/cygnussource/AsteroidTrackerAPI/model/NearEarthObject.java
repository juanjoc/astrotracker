package com.cygnussource.AsteroidTrackerAPI.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class NearEarthObject {

    private final static String EARTH = "Earth";

    private final String id;
    @SerializedName( "neo_reference_id" )
    private final String neoReferenceId;
    private final String name;
    @SerializedName( "nasa_jpl_url" )
    private final String nasaJPLUrl;
    @SerializedName( "absolute_magnitude_h" )
    private final Double absoluteMagnitudeH;
    @SerializedName( value = "estimated_diameter" )
    private final Map<String, Diameters> estimatedDiameter;
    @SerializedName( "is_potentially_hazardous_asteroid" )
    private final Boolean potentiallyHazardousAsteroid;
    @SerializedName( "close_approach_data" )
    private final List<CloseApproachData> closeApproachData;
    @SerializedName( "is_sentry_object" )
    private final Boolean sentryObject;
    @SerializedName( "orbital_data" )
    private final OrbitalData orbitalData;

    @Data
    public class Diameters {
        @SerializedName( "estimated_diameter_min" )
        private Double min;
        @SerializedName( "estimated_diameter_max" )
        private Double max;

    }

    @Data
    public class CloseApproachData {
        @SerializedName( "close_approach_date" )
        private LocalDate closeApproachDate;
        @SerializedName( "epoch_date_close_approach" )
        private Long epochDateCloseApproach;
        @SerializedName( "relative_velocity" )
        private Map<String, Double> relativeVelocity;
        @SerializedName( "miss_distance" )
        private Map<String, Double> missDistance;
        @SerializedName( "orbiting_body" )
        private String orbitingBody = EARTH;
    }

    @Data
    public class OrbitalData {
        @SerializedName( "orbit_id" )
        private String orbitId;
        @SerializedName( "orbit_determination_date")
        private Date orbitDetermination;
        @SerializedName( "first_observation_date" )
        private Date firstObservation;;
        @SerializedName( "last_observation_date" )
        private Date lastObservation;
        @SerializedName( "data_arc_in_days" )
        private Long dataArcInDays;
        @SerializedName( "observations_used")
        private Integer observationsUsed;
        @SerializedName( "orbit_uncertainty")
        private Integer orbitUncertainty;
        @SerializedName( "minimum_orbit_intersection" )
        private String minimunOrbitIntersection;
        @SerializedName( "jupiter_tisserand_invariant" )
        private Double jupiterTisserandInvariant;
        @SerializedName( "epoch_osculation" )
        private Double epochOsculation;
        @SerializedName( "eccentricity" )
        private String eccentricity;
        @SerializedName( "semi_major_axis" )
        private Double semiMajorAxis;
        private Double inclination;
        @SerializedName( "ascending_node_longitude" )
        private Double ascendingNodeLongitude;
        @SerializedName( "orbital_period" )
        private Double orbitalPeriod;
        @SerializedName( "perihelion_distance" )
        private Double perihelionDistance;
        @SerializedName( "perihelion_argument" )
        private Double perihelionArgument;
        @SerializedName( "aphelion_distance" )
        private Double aphelionDistance;
        @SerializedName( "perihelion_time" )
        private Double perihelionTime;
        @SerializedName( "mean_anomaly" )
        private Double meanAnomaly;
        @SerializedName( "mean_motion" )
        private Double meanMotion;
        @SerializedName( "equinox" )
        private String equinox;
        @SerializedName( "orbit_class" )
        private OrbitClass orbitClass;

    }

    @Data
    public class OrbitClass {
        @SerializedName( "orbit_class_type" )
        private String type;
        @SerializedName( "orbit_class_description" )
        private String description;
        @SerializedName( "orbit_class_range" )
        private String range;
    }

}