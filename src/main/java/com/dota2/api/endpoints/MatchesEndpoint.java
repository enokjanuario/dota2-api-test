package com.dota2.api.endpoints;

import com.dota2.api.client.RestClient;
import com.dota2.constants.EndpointConstants;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class MatchesEndpoint {
    private static final Logger logger = LogManager.getLogger(MatchesEndpoint.class);
    private final RestClient restClient;

    public MatchesEndpoint() {
        this.restClient = new RestClient();
    }

    /**
     * Gets recent public matches
     * @return Response object
     */
    public Response getRecentMatches() {
        logger.info("Getting recent public matches");
        return restClient.get(EndpointConstants.MATCHES_ENDPOINT);
    }

    /**
     * Gets a match by ID
     * @param matchId match ID
     * @return Response object
     */
    public Response getMatchById(long matchId) {
        logger.info("Getting match with ID: {}", matchId);
        String endpoint = EndpointConstants.MATCH_BY_ID_ENDPOINT.replace("{id}", String.valueOf(matchId));
        return restClient.get(endpoint);
    }

    /**
     * Gets matches by parameters
     * @param parameters map of query parameters
     * @return Response object
     */
    public Response getMatchesByParams(Map<String, Object> parameters) {
        logger.info("Getting matches with parameters: {}", parameters);
        return restClient.get(EndpointConstants.MATCHES_ENDPOINT, parameters);
    }
}