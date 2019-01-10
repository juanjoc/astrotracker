package com.cygnussource.AsteroidTrackerAPI.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer implements JsonSerializer<LocalDate> {
    @Override
    public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {

        if (localDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "YYYY-MM-dd" );
            return new JsonPrimitive( localDate.format(formatter) );
        }

        return new JsonPrimitive( "" );
    }
}
