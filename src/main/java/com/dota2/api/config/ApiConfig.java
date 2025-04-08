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
        try (InputStream inputStream = getClass().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                properties.load(inputStream);
                logger.info("API configuration loaded successfully");
            } else {
                logger.error("Unable to find config file: {}", CONFIG_FILE);
                throw new RuntimeException("Configuration file not found: " + CONFIG_FILE);
            }
        } catch (IOException e) {
            logger.error("Error loading API configuration: {}", e.getMessage(), e);
            throw new RuntimeException("Error loading API configuration", e);
        }
    }

    public String getBaseUrl() {
        return properties.getProperty("api.base.url");
    }

    public int getDefaultTimeout() {
        return Integer.parseInt(properties.getProperty("api.timeout.seconds", "30"));
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}