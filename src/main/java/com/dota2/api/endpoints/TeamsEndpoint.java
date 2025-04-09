package com.dota2.api.endpoints;

import com.dota2.api.client.RestClient;
import com.dota2.constants.EndpointConstants;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Endpoint class for Teams API
 */
public class TeamsEndpoint {
    private static final Logger logger = LogManager.getLogger(TeamsEndpoint.class);
    private final RestClient restClient;

    public TeamsEndpoint() {
        this.restClient = new RestClient();
    }

    /**
     * Gets all teams
     * @return Response object with list of teams
     */
    public Response getAllTeams() {
        logger.info("Getting all teams");
        return restClient.get(EndpointConstants.TEAMS_ENDPOINT);
    }

    /**
     * Gets a team by ID
     * @param teamId Team ID
     * @return Response object with team data
     */
    public Response getTeamById(int teamId) {
        logger.info("Getting team with ID: {}", teamId);
        String endpoint = EndpointConstants.TEAM_BY_ID_ENDPOINT.replace("{id}", String.valueOf(teamId));
        return restClient.get(endpoint);
    }

    /**
     * Gets teams with pagination parameters
     * @param parameters Map containing pagination parameters (limit, offset)
     * @return Response object with paginated teams
     */
    public Response getTeamsWithParams(Map<String, Object> parameters) {
        logger.info("Getting teams with parameters: {}", parameters);
        return restClient.get(EndpointConstants.TEAMS_ENDPOINT, parameters);
    }

    /**
     * Gets matches for a specific team
     * @param teamId Team ID
     * @return Response object with team matches
     */
    public Response getTeamMatches(int teamId) {
        logger.info("Getting matches for team with ID: {}", teamId);
        String endpoint = EndpointConstants.TEAM_BY_ID_ENDPOINT.replace("{id}", String.valueOf(teamId)) + "/matches";
        return restClient.get(endpoint);
    }

    /**
     * Gets players for a specific team
     * @param teamId Team ID
     * @return Response object with team players
     */
    public Response getTeamPlayers(int teamId) {
        logger.info("Getting players for team with ID: {}", teamId);
        String endpoint = EndpointConstants.TEAM_BY_ID_ENDPOINT.replace("{id}", String.valueOf(teamId)) + "/players";
        return restClient.get(endpoint);
    }

    /**
     * Gets heroes played by a specific team
     * @param teamId Team ID
     * @return Response object with heroes played by team
     */
    public Response getTeamHeroes(int teamId) {
        logger.info("Getting heroes for team with ID: {}", teamId);
        String endpoint = EndpointConstants.TEAM_BY_ID_ENDPOINT.replace("{id}", String.valueOf(teamId)) + "/heroes";
        return restClient.get(endpoint);
    }
}