package com.apitest;

import com.apitest.config.AppConfig;
import com.apitest.dataModel.TriangleResponseModel;
import com.apitest.dataModel.TriangleRequestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static com.apitest.matchers.EqualTriangleMatcher.triangleHasDimensions;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
public class CreateTriangleTest extends BaseTriangleTest {

    @Test
    @DisplayName("Create one triangle")
    public void createOneTriangle() {
        ResponseEntity<TriangleResponseModel> response = createTriangle("1:2:3", ":");
        assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat("Triangle has incorrect sides", response.getBody(), triangleHasDimensions(1D, 2D, 3D));
        assertThat("Triangle has empty id", response.getBody().getId(), not(isEmptyOrNullString()));
    }

    @Test
    @DisplayName("Create one triangle with default separator")
    public void createTriangleWithDefaultSeparator() {
        TriangleRequestModel triangle = TriangleRequestModel.builder().input("1;2;3").build();
        ResponseEntity<TriangleResponseModel> response = restService.postForObject(baseUrl, "/triangle", headers, triangle, TriangleResponseModel.class);
        assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat("Triangle has incorrect sides", response.getBody(), triangleHasDimensions(1D, 2D, 3D));
        assertThat("Triangle has empty id", response.getBody().getId(), not(isEmptyOrNullString()));
    }

    @Test
    @DisplayName("Create one triangle with incorrect Authorization token")
    public void createTriangleWithIncorrectAuthorizationToken() {
        headers.set("X-User", "incorrect_token");
        ResponseEntity<TriangleResponseModel> response = createTriangle("1;2;3", ";");
        assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));
    }

    @Test
    @DisplayName("Create one triangle with empty string separator")
    public void createTriangleWithEmptyStringSeparator() {
        ResponseEntity<TriangleResponseModel> response = createTriangle("1;2;3", "");
        assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.UNPROCESSABLE_ENTITY));
        assertThat("There is wrong response message! ", response.getBody().getMessage(), equalTo("Cannot process input"));
    }

    @Test
    @DisplayName("Create one triangle with two sides")
    public void createTriangleWith2Sides() {
        ResponseEntity<TriangleResponseModel> response = createTriangle("1;2", ";");
        assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.UNPROCESSABLE_ENTITY));
        assertThat("There is wrong response message! ", response.getBody().getMessage(), equalTo("Cannot process input"));
    }

    @Test
    @DisplayName("Create 10 triangles")
    public void create10Triangles() {
        for (int i = 0; i < 10; i++) {
            ResponseEntity<TriangleResponseModel> response = createTriangle(TriangleRequestModel.randomInputWithSeparator(";", 100), ";");
            assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.OK));
        }
        ResponseEntity<TriangleResponseModel[]> allTriangles = restService.getForObject(baseUrl, headers, TriangleResponseModel[].class, "/triangle/all");
        assertThat("There are no triangles!", allTriangles.getBody(), notNullValue());
        assertThat("There is wrong number of triangles!", Arrays.asList(allTriangles.getBody()), hasSize(10));
    }

    @Test
    @DisplayName("Create 11 triangles")
    public void create11Triangles() {
        for (int i = 0; i < 10; i++) {
            ResponseEntity<TriangleResponseModel> response = createTriangle(TriangleRequestModel.randomInputWithSeparator(";", 100), ";");
            assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.OK));
        }
        ResponseEntity<TriangleResponseModel> response = createTriangle(TriangleRequestModel.randomInputWithSeparator(";", 100), ";");
        assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.UNPROCESSABLE_ENTITY));
        assertThat("There is wrong response message! ", response.getBody().getMessage(), equalTo("Cannot process input"));
    }
}