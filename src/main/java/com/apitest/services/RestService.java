package com.apitest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class RestService<T> {

    @Autowired
    RestTemplate restTemplate;

    private URI buildUrl(String baseUrl, String... pathSegments) {
        return UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .pathSegment(pathSegments)
                .build()
                .toUri();
    }

    public ResponseEntity<T> getForObject(String baseUrl, HttpHeaders headers, Class T, String endpoint) {
        URI uri = buildUrl(baseUrl, endpoint);
        return getForObject(uri, headers, T);
    }

    public ResponseEntity<T> getForObject(String baseUrl, HttpHeaders headers, Class T, String... endpoints) {
        URI uri = buildUrl(baseUrl, endpoints);
        return getForObject(uri, headers, T);
    }

    public ResponseEntity<T> getForObject(URI uri, HttpHeaders headers, Class T) {
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(
                uri, HttpMethod.GET, entity, T);
    }

    public ResponseEntity<T> postForObject(String baseUrl, String endpoint, HttpHeaders headers, Object dataObject, Class T) {
        URI uri = buildUrl(baseUrl, endpoint);
        HttpEntity entity = new HttpEntity(dataObject, headers);
        return restTemplate.exchange(
                uri, HttpMethod.POST, entity, T);
    }

    public ResponseEntity<T> deleteForObject(String baseUrl, HttpHeaders headers, Class T, String... endpoints) {
        URI uri = buildUrl(baseUrl, endpoints);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(
                uri, HttpMethod.DELETE, entity, T);
    }
}
