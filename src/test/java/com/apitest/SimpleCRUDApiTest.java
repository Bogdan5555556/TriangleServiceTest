package com.apitest;

import com.apitest.config.AppConfig;
import com.apitest.dataModel.TriangleGetModel;
import com.apitest.dataModel.TrianglePostModel;
import com.apitest.services.RestService;
import com.apitest.services.Tokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Objects;

import static com.apitest.matchers.EqualTriangleMatcher.triangleHasDimensions;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes= AppConfig.class)
public class SimpleCRUDApiTest {

    private HttpHeaders headers;

    @Autowired
    RestService restService;

    @Autowired
    Tokenizer tokenizer;

    @Value("${base.url}")
    private String baseUrl;

    @BeforeEach
    public void prepareDefaultHeaders() {
        headers = new HttpHeaders();
        headers.set("X-User", tokenizer.getToken());
    }

    @BeforeEach
    public void deleteAllTriangles() {
        ResponseEntity<TriangleGetModel[]> allTriangles = restService.getForObject(baseUrl, headers, TriangleGetModel[].class, "/triangle/all");
        Arrays.stream(Objects.requireNonNull(allTriangles.getBody())).forEach(triangle -> {
            restService.deleteForObject(baseUrl, headers, TriangleGetModel[].class, "/triangle", triangle.getId());
        });
    }

    @Test
    public void createOneTriangle() {
        TrianglePostModel triangle = TrianglePostModel.builder().input("1:2:3").separator(":").build();
        ResponseEntity<TriangleGetModel> response = restService.postForObject(baseUrl, "/triangle", headers, triangle, TriangleGetModel.class);
        assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat("Triangle has incorrect sides", response.getBody(),triangleHasDimensions(1D,2D,3D));
        assertThat("Triangle has empty id", response.getBody().getId(),not(isEmptyOrNullString()));
    }

    @Test
    public void createTriangleWithDefaultSeparator() {
        TrianglePostModel triangle = TrianglePostModel.builder().input("1;2;3").build();
        ResponseEntity<TriangleGetModel> response = restService.postForObject(baseUrl, "/triangle", headers, triangle, TriangleGetModel.class);
        assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat("Triangle has incorrect sides", response.getBody(),triangleHasDimensions(1D,2D,3D));
        assertThat("Triangle has empty id", response.getBody().getId(),not(isEmptyOrNullString()));
    }

    @Test
    public void createTriangleWithEmptyStringSeparator() {
        TrianglePostModel triangle = TrianglePostModel.builder().input("1;2;3").separator("").build();
        ResponseEntity<TriangleGetModel> response = restService.postForObject(baseUrl, "/triangle", headers, triangle, TriangleGetModel.class);
        assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat("Triangle has incorrect sides", response.getBody(),triangleHasDimensions(1D,2D,3D));
        assertThat("Triangle has empty id", response.getBody().getId(),not(isEmptyOrNullString()));
    }

    @Test
    public void createTriangleWith2Sides() {
        TrianglePostModel triangle = TrianglePostModel.builder().input("1;2").separator("").build();
        ResponseEntity<TriangleGetModel> response = restService.postForObject(baseUrl, "/triangle", headers, triangle, TriangleGetModel.class);
        assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.UNPROCESSABLE_ENTITY));
        assertThat("There is wrong response message! ",response.getBody().getMessage(), equalTo("Cannot process input"));
    }
}