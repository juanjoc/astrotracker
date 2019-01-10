package com.cygnussource.AsteroidTrackerAPI.clients;

import org.springframework.http.HttpStatus;

import java.util.concurrent.CompletableFuture;

public interface HttpClient {

    CompletableFuture<String> get(String uri);

    String post(String uri, String json);

    void put(String uri, String json);

    void delete(String uri);

    HttpStatus getStatus();

    void setStatus(HttpStatus status);

}
