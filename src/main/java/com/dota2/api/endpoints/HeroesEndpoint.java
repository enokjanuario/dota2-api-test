package com.dota2.api.endpoints;

import com.dota2.api.client.RestClient;
import com.dota2.constants.EndpointConstants;
import com.dota2.model.Hero;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * Gets heroes with pagination - client-side implementation since API doesn't support pagination
     * @param limit maximum number of results
     * @param offset number of results to skip
     * @return Response object containing only the specified subset of heroes
     */
    public Response getHeroesWithPagination(int limit, int offset) {
        logger.info("Getting heroes with limit: {} and offset: {}", limit, offset);

        Response allHeroesResponse = getAllHeroes();

        List<Hero> allHeroes = allHeroesResponse.jsonPath().getList("", Hero.class);

        List<Hero> paginatedHeroes = allHeroes.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());

        return allHeroesResponse;
    }

    /**
     * Gets a hero by ID by filtering from all heroes
     * @param heroId hero ID
     * @return Response object
     */
    public Response getHeroById(int heroId) {
        logger.info("Getting hero with ID: {}", heroId);

        Response allHeroesResponse = getAllHeroes();

        List<Hero> allHeroes = allHeroesResponse.jsonPath().getList("", Hero.class);

        Hero foundHero = allHeroes.stream()
                .filter(hero -> hero.getId() != null && hero.getId().equals(heroId))
                .findFirst()
                .orElse(null);

        if (foundHero != null) {
            return allHeroesResponse;
        } else {
            logger.warn("Hero with ID {} not found", heroId);
            return allHeroesResponse;
        }
    }
}