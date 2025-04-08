package com.dota2.api.client;

import com.dota2.api.config.ApiConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * REST Client for making API requests.
 * Uses Rest-Assured for HTTP requests.
 */
public class RestClient {
    private static final Logger logger = LogManager.getLogger(RestClient.class);
    private final ApiConfig apiConfig;
    private final RequestSpecification requestSpec;

    public RestClient() {
        this.apiConfig = ApiConfig.getInstance();
        this.requestSpec = buildRequestSpec();
    }

    private RequestSpecification buildRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(apiConfig.getBaseUrl())
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())  // For Allure reporting
                .log(LogDetail.ALL)
                .build();
    }

    public Response get(String endpoint) {
        logger.info("Executing GET request to: {}", endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(endpoint)
                .then()
                .log().ifValidationFails()
                .extract().response();
    }

    public Response get(String endpoint, Map<String, ?> queryParams) {
        logger.info("Executing GET request to: {} with params: {}", endpoint, queryParams);
        return RestAssured.given()
                .spec(requestSpec)
                .queryParams(queryParams)
                .when()
                .get(endpoint)
                .then()
                .log().ifValidationFails()
                .extract().response();
    }

    public Response post(String endpoint, Object requestBody) {
        logger.info("Executing POST request to: {}", endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(endpoint)
                .then()
                .log().ifValidationFails()
                .extract().response();
    }

    public Response put(String endpoint, Object requestBody) {
        logger.info("Executing PUT request to: {}", endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .put(endpoint)
                .then()
                .log().ifValidationFails()
                .extract().response();
    }

    public Response delete(String endpoint) {
        logger.info("Executing DELETE request to: {}", endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .when()
                .delete(endpoint)
                .then()
                .log().ifValidationFails()
                .extract().response();
    }
}