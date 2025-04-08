package com.dota2.utils;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

/**
 * Utility class for JSON Schema validation.
 */
public class JsonSchemaValidator {
    private static final Logger logger = LogManager.getLogger(JsonSchemaValidator.class);
    private static final String SCHEMA_BASE_PATH = "/schemas/";

    private JsonSchemaValidator() {

    }

    /**
     * Validates a response against a JSON schema
     * @param response API response to validate
     * @param schemaFileName name of the schema file (without path)
     */
    public static void validateSchema(Response response, String schemaFileName) {
        logger.info("Validating response against schema: {}", schemaFileName);
        String schemaPath = SCHEMA_BASE_PATH + schemaFileName;

        try {
            InputStream schemaStream = JsonSchemaValidator.class.getResourceAsStream(schemaPath);
            if (schemaStream == null) {
                throw new IllegalArgumentException("Schema file not found: " + schemaPath);
            }

            response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(schemaStream));
            logger.info("Schema validation successful");
        } catch (Exception e) {
            logger.error("Schema validation failed: {}", e.getMessage(), e);
            throw new AssertionError("Schema validation failed: " + e.getMessage(), e);
        }
    }
}