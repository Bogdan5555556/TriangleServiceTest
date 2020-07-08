package com.apitest;

import com.apitest.config.AppConfig;
import com.apitest.dataModel.TriangleResponseModel;
import com.apitest.dataModel.TriangleRequestModel;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Random;

import static com.apitest.matchers.EqualTriangleMatcher.triangleHasDimensions;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
public class GetTriangleTest extends BaseTriangleTest {

    @Test
    @Description("Get all triangles")
    public void getAllTriangles() {
        int triangleNumbers = 1 + new Random().nextInt(9);
        for (int i = 0; i < triangleNumbers; i++) {
            ResponseEntity<TriangleResponseModel> response = createTriangle(TriangleRequestModel.randomInputWithSeparator(";",100),";");
            assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.OK));
        }
        ResponseEntity<TriangleResponseModel[]> allTriangles = restService.getForObject(baseUrl, headers, TriangleResponseModel[].class, "/triangle/all");
        assertThat("There are no triangles!", allTriangles.getBody(), notNullValue());
        assertThat("There is wrong number of triangles!", Arrays.asList(allTriangles.getBody()), hasSize(triangleNumbers));
    }

    @Test
    @Description("Get triangle by id")
    public void getTriangleById() {
        ResponseEntity<TriangleResponseModel> response = createTriangle("1;2;3",";");
        assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.OK));
        ResponseEntity<TriangleResponseModel> responseTriangle = restService.getForObject(baseUrl, headers, TriangleResponseModel.class, "/triangle", response.getBody().getId());
        assertThat("There is wrong response status! ", responseTriangle.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat("Triangle has incorrect sides", responseTriangle.getBody(), triangleHasDimensions(1D, 2D, 3D));
        assertThat("Triangle has incorrect id", responseTriangle.getBody().getId(), equalTo(response.getBody().getId()));
    }

    @Test
    @Description("Get triangle by incorrect id")
    public void getTriangleByIncorrectId() {
        ResponseEntity<TriangleResponseModel> response = createTriangle("1;2;3",";");
        ResponseEntity<TriangleResponseModel> responseTriangle = restService.getForObject(baseUrl, headers, TriangleResponseModel.class, "/triangle", "incorrect_id");
        assertThat("There is wrong response status! ", responseTriangle.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    @Description("Get all triangles with incorrect Authorisation token")
    public void getAllTrianglesWithIncorrectAuthorizationToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User", "incorrect_token");
        ResponseEntity<TriangleResponseModel> allTriangles = restService.getForObject(baseUrl, headers, TriangleResponseModel.class, "/triangle/all");
        assertThat("There is wrong response status! ", allTriangles.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));
    }

    @Test
    @Description("Get triangle by id with incorrect Authorisation token")
    public void getTriangleByIdWithIncorrectAuthorizationToken() {
        ResponseEntity<TriangleResponseModel> response = createTriangle("1;2;3",";");
        assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.OK));
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User", "incorrect_token");
        ResponseEntity<TriangleResponseModel> responseTriangle = restService.getForObject(baseUrl, headers, TriangleResponseModel.class, "/triangle", response.getBody().getId());
        assertThat("There is wrong response status! ", responseTriangle.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));
    }

    @Test
    @Description("Get triangle area")
    public void getTriangleArea() {
        ResponseEntity<TriangleResponseModel> response = createTriangle("5;6;7",";");
        assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.OK));
        ResponseEntity<TriangleResponseModel> responseTriangle = restService.getForObject(baseUrl, headers, TriangleResponseModel.class, "/triangle", response.getBody().getId(), "/area");
        assertThat("There is wrong response status! ", responseTriangle.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat("There is wrong triangle area! ",responseTriangle.getBody().getResult(), equalTo(calculateTriangleArea(response.getBody())));
    }

    @Test
    @Description("Get triangle area with multiple triangles")
    public void getTriangleAreaMultipleTriangles() {
        for(int i = 0; i<10; i++){
            ResponseEntity<TriangleResponseModel> response = createTriangle(TriangleRequestModel.randomInputWithSeparator(";",100),";");
            assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.OK));
            ResponseEntity<TriangleResponseModel> responseTriangle = restService.getForObject(baseUrl, headers, TriangleResponseModel.class, "/triangle", response.getBody().getId(), "/area");
            assertThat("There is wrong response status! ", responseTriangle.getStatusCode(), equalTo(HttpStatus.OK));
            assertThat("There is wrong triangle area! ",responseTriangle.getBody().getResult(), equalTo(calculateTriangleArea(response.getBody())));
        }
    }

    @Test
    @Description("Get triangle area for triangle with zero area")
    public void getTriangleWithZeroArea() {
        ResponseEntity<TriangleResponseModel> response = createTriangle("1;2;3",";");
        assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.OK));
        ResponseEntity<TriangleResponseModel> responseTriangle = restService.getForObject(baseUrl, headers, TriangleResponseModel.class, "/triangle", response.getBody().getId(), "/area");
        assertThat("There is wrong response status! ", responseTriangle.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat("There is wrong triangle area! ",responseTriangle.getBody().getResult(), equalTo(calculateTriangleArea(response.getBody())));
    }

    @Test
    @Description("Get triangle perimeter")
    public void getTrianglePerimeter() {
        ResponseEntity<TriangleResponseModel> response = createTriangle("5;6;7",";");
        assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.OK));
        ResponseEntity<TriangleResponseModel> responseTriangle = restService.getForObject(baseUrl, headers, TriangleResponseModel.class, "/triangle", response.getBody().getId(), "/perimeter");
        assertThat("There is wrong response status! ", responseTriangle.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat("There is wrong triangle perimeter! ",responseTriangle.getBody().getResult(), equalTo(calculateTrianglePerimeter(response.getBody())));
    }

    @Test
    @Description("Get triangle perimeter with multiple triangles")
    public void getTrianglePerimeterMultipleTriangles() {
        for(int i = 0; i<10; i++){
            ResponseEntity<TriangleResponseModel> response = createTriangle(TriangleRequestModel.randomInputWithSeparator(";",100),";");
            assertThat("There is wrong response status! ", response.getStatusCode(), equalTo(HttpStatus.OK));
            ResponseEntity<TriangleResponseModel> responseTriangle = restService.getForObject(baseUrl, headers, TriangleResponseModel.class, "/triangle", response.getBody().getId(), "/perimeter");
            assertThat("There is wrong response status! ", responseTriangle.getStatusCode(), equalTo(HttpStatus.OK));
            assertThat("There is wrong triangle perimeter! ",responseTriangle.getBody().getResult(), equalTo(calculateTrianglePerimeter(response.getBody())));
        }
    }

    @Test
    @Description("Get triangle area with incorrect Authorization token")
    public void getTriangleAreaWithIncorrectAuthorizationToken() {
        ResponseEntity<TriangleResponseModel> response = createTriangle("5;6;7",";");
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User", "incorrect_token");
        ResponseEntity<TriangleResponseModel> responseTriangle = restService.getForObject(baseUrl, headers, TriangleResponseModel.class, "/triangle", response.getBody().getId(), "/area");
        assertThat("There is wrong response status! ", responseTriangle.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));
    }

    @Test
    @Description("Get triangle perimeter with incorrect Authorization token")
    public void getTrianglePerimeterWithIncorrectAuthorizationToken() {
        ResponseEntity<TriangleResponseModel> response = createTriangle("5;6;7",";");
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User", "incorrect_token");
        ResponseEntity<TriangleResponseModel> responseTriangle = restService.getForObject(baseUrl, headers, TriangleResponseModel.class, "/triangle", response.getBody().getId(), "/area");
        assertThat("There is wrong response status! ", responseTriangle.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));
    }
}
