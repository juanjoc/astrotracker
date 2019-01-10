package com.cygnussource.AsteroidTrackerAPI.repositories.impl;

import com.cygnussource.AsteroidTrackerAPI.clients.impl.HttpClientImpl;
import com.cygnussource.AsteroidTrackerAPI.model.LocalDateDeserializer;
import com.cygnussource.AsteroidTrackerAPI.model.NearEarthObject;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;

@Component
public class AsteroidTrackerRepositoryImpl {

    @Autowired
    private Environment environment;

    public Optional<NearEarthObject> findById(@NotNull String id) throws Exception {

        GsonBuilder gson = new GsonBuilder().setDateFormat( "yyyy-MM-dd HH:mm:ss" );
        gson.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());

        return Optional.ofNullable(new HttpClientImpl().get(composeURI(id)).thenApply(s -> gson.create().fromJson(s, NearEarthObject.class)).get());
    }

    private String composeURI( String id ) {
        return new StringBuilder(environment.getProperty("nasa.server")).append(environment.getProperty("nasa.neoLookup")).append(id).append("?api_key=").append(environment.getProperty("nasa.token")).toString();
    }
}
