package com.dota2.api.endpoints;

import com.dota2.api.client.RestClient;
import com.dota2.constants.EndpointConstants;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Endpoint class for Heroes API
 */
public class HeroesEndpoint {
    private static final Logger logger = LogManager.getLogger(HeroesEndpoint.class);
    private final RestClient restClient;

    public HeroesEndpoint() {
        this.restClient = new RestClient();
    }

    /**
     * Gets all heroes
     * @return Response object
     */
    public Response getAllHeroes() {
        logger.info("Getting all heroes");
        return restClient.get(EndpointConstants.HEROES_ENDPOINT);
    }

    /**
     * Gets heroes with pagination
     * @param limit maximum number of results
     * @param offset number of results to skip
     * @return Response object
     */
    public Response getHeroesWithPagination(int limit, int offset) {
        logger.info("Getting heroes with limit: {} and offset: {}", limit, offset);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put(EndpointConstants.LIMIT_PARAM, limit);
        queryParams.put(EndpointConstants.OFFSET_PARAM, offset);

        return restClient.get(EndpointConstants.HEROES_ENDPOINT, queryParams);
    }

    /**
     * Gets a hero by ID
     * @param heroId hero ID
     * @return Response object
     */
    public Response getHeroById(int heroId) {
        logger.info("Getting hero with ID: {}", heroId);
        String endpoint = EndpointConstants.HERO_BY_ID_ENDPOINT.replace("{id}", String.valueOf(heroId));
        return restClient.get(endpoint);
    }
}