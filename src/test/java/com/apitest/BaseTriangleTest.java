package com.apitest;

import com.apitest.dataModel.TriangleRequestModel;
import com.apitest.dataModel.TriangleResponseModel;
import com.apitest.services.RestService;
import com.apitest.services.Tokenizer;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Objects;

public class BaseTriangleTest {

    HttpHeaders headers;

    @Autowired
    RestService restService;

    @Autowired
    Tokenizer tokenizer;

    @Value("${base.url}")
    String baseUrl;

    @BeforeEach
    public void prepareDefaultHeaders() {
        headers = new HttpHeaders();
        headers.set("X-User", tokenizer.getToken());
    }

    @BeforeEach
    public void deleteAllTriangles() {
        ResponseEntity<TriangleResponseModel[]> allTriangles = restService.getForObject(baseUrl, headers, TriangleResponseModel[].class, "/triangle/all");
        Arrays.stream(Objects.requireNonNull(allTriangles.getBody())).forEach(triangle -> {
            restService.deleteForObject(baseUrl, headers, TriangleResponseModel[].class, "/triangle", triangle.getId());
        });
    }

    @Step("Create triangle with input {input} and separator {separator}.")
    public ResponseEntity<TriangleResponseModel> createTriangle(String input, String separator) {
        TriangleRequestModel triangle = TriangleRequestModel.builder().input(input).separator(separator).build();
        return restService.postForObject(baseUrl, "/triangle", headers, triangle, TriangleResponseModel.class);
    }

    public Double calculateTriangleArea(TriangleResponseModel responseModel) {
        double firstSide = responseModel.getFirstSide();
        double secondSide = responseModel.getSecondSide();
        double thirdSide = responseModel.getThirdSide();
        double result = (firstSide + secondSide + thirdSide) / 2;
        return Math.sqrt(result * (result - firstSide) * (result - secondSide) * (result - thirdSide));
    }

    public Double calculateTrianglePerimeter(TriangleResponseModel responseModel) {
        double firstSide = responseModel.getFirstSide();
        double secondSide = responseModel.getSecondSide();
        double thirdSide = responseModel.getThirdSide();
        return firstSide + secondSide + thirdSide;
    }
}
