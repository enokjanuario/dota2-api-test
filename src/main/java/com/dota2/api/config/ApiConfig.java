package com.dota2.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Configuration class for API tests.
 * Loads properties from api-config.properties file.
 */
public class ApiConfig {
    private static final Logger logger = LogManager.getLogger(ApiConfig.class);
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "/config/api-config.properties";
    private static ApiConfig instance;

    private ApiConfig() {
        loadProperties();
    }

    public static synchronized ApiConfig getInstance() {
        if (instance == null) {
            instance = new ApiConfig();
        }
        return instance;
    }

    private void loadProperties() {

    }

    public String getBaseUrl() {
        return properties.getProperty("api.base.url");
    }

}