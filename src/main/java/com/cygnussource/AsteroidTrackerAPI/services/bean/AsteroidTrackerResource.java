package com.cygnussource.AsteroidTrackerAPI.services.bean;

import com.cygnussource.AsteroidTrackerAPI.controllers.AsteroidController;
import com.cygnussource.AsteroidTrackerAPI.model.NearEarthObject;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Getter
public class AsteroidTrackerResource extends ResourceSupport {

    private final AsteroidTrackerBean asteroidTrackerBean;

    public AsteroidTrackerResource(final NearEarthObject nearEarthObject, final MissDistanceUnit missDistanceUnit) {

        AsteroidTrackerBean.Builder builder =
        new AsteroidTrackerBean.Builder()
                .withId(nearEarthObject.getId(), nearEarthObject.getName())
//                .withCloseApproachingDate( nearEarthObject.getCloseApproachData().get(0).getCloseApproachDate() )
                .withNeoReferenceID(nearEarthObject.getNeoReferenceId())
                .withPotentiallyHazardous(nearEarthObject.getPotentiallyHazardousAsteroid());
//                .withMissDistance(missDistanceUnitiyFunction.apply(nearEarthObject.getCloseApproachData().get(0).getMissDistance(), missDistanceUnit));

        asteroidTrackerBean = builder.build();

        try {
//            add(linkTo(methodOn(AsteroidController.class).getById(nearEarthObject.getId(), missDistanceUnit.name())).withSelfRel());
            add(linkTo(AsteroidController.class, nearEarthObject.getId()).withRel(Link.REL_NEXT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
