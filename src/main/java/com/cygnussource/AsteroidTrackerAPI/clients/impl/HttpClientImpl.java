package com.cygnussource.AsteroidTrackerAPI.clients.impl;

import com.cygnussource.AsteroidTrackerAPI.clients.HttpClient;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Component
public class HttpClientImpl implements HttpClient {

    private RestTemplate rest;
    private HttpHeaders headers;
    private HttpStatus status;

    public HttpClientImpl() {
        this.rest = new RestTemplate();
        this.headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");
    }

    @Override
    public CompletableFuture<String> get(String uri) {
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);

        return CompletableFuture.supplyAsync( () -> {
            ResponseEntity<String> responseEntity = rest.exchange(uri, HttpMethod.GET, requestEntity, String.class);
            this.setStatus(responseEntity.getStatusCode());
            return responseEntity.getBody();
        } );
    }

    @Override
    public String post(String uri, String json) {
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
        ResponseEntity<String> responseEntity = rest.exchange(uri, HttpMethod.POST, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    @Override
    public void put(String uri, String json) {
//        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
//        ResponseEntity<String> responseEntity = rest.exchange( uri, HttpMethod.PUT, requestEntity, null);
//        this.setStatus(responseEntity.getStatusCode());
    }

    @Override
    public void delete(String uri) {
//        HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
//        ResponseEntity<String> responseEntity = rest.exchange( uri, HttpMethod.DELETE, requestEntity, null);
//        this.setStatus(responseEntity.getStatusCode());
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
