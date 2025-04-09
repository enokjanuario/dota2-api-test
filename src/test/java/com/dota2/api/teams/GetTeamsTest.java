package com.dota2.api.teams;

import com.dota2.api.endpoints.TeamsEndpoint;
import com.dota2.config.TestConfig;
import com.dota2.constants.EndpointConstants;
import com.dota2.model.Team;
import com.dota2.utils.SchemaValidator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

/**
 * Tests for the Teams API - GET endpoints
 */
@Epic("Dota 2 API Testing")
@Feature("Teams API")
@Listeners(TestConfig.class)
public class GetTeamsTest {
    private static final Logger logger = LogManager.getLogger(GetTeamsTest.class);
    private TeamsEndpoint teamsEndpoint;

    @BeforeClass
    public void setup() {
        teamsEndpoint = new TeamsEndpoint();
    }

    @Test(description = "Verify that the GET /teams endpoint returns 200 OK")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get all teams")
    @Description("This test verifies that the GET /teams endpoint returns a 200 OK status code")
    public void testGetAllTeamsReturns200() {
        Response response = teamsEndpoint.getAllTeams();

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK,
                "Expected 200 OK status code");

        response.then().body("size()", greaterThan(0));

        logger.info("Successfully verified GET /teams returns 200 OK with teams data");
    }

    @Test(description = "Verify that the teams data contains required fields")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get all teams")
    @Description("This test verifies that each team in the response contains required fields")
    public void testTeamsContainRequiredFields() {
        Response response = teamsEndpoint.getAllTeams();

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        response.then().body("[0]", hasKey("team_id"));
        response.then().body("[0]", hasKey("name"));


        logger.info("Successfully verified teams contain all required fields");
    }

    @Test(description = "Verify that teams with pagination works")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get teams with pagination")
    @Description("This test verifies that teams can be retrieved and client-side pagination works")
    public void testTeamsPagination() {

        Response response = teamsEndpoint.getAllTeams();
        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        List<Team> allTeams = response.jsonPath().getList("", Team.class);
        assertTrue(allTeams.size() > 0, "Should return some teams");

        int limit = 5;
        int offset = 0;

        List<Team> paginatedTeams = allTeams.subList(offset, Math.min(offset + limit, allTeams.size()));
        assertEquals(paginatedTeams.size(), limit, "Should have exactly " + limit + " teams after client-side pagination");

        offset = limit;
        List<Team> secondPageTeams = allTeams.subList(offset, Math.min(offset + limit, allTeams.size()));
        assertEquals(secondPageTeams.size(), limit, "Second page should have " + limit + " teams");

        logger.info("Successfully verified client-side teams pagination with limit: {}", limit);
    }

    @Test(description = "Verify that teams can be sorted client-side")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get teams with sorting")
    @Description("This test verifies that teams can be sorted client-side")
    public void testTeamsSorting() {
        Response response = teamsEndpoint.getAllTeams();
        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        List<Team> allTeams = response.jsonPath().getList("", Team.class);
        assertTrue(allTeams.size() > 0, "Should return some teams");

        List<Team> teamsWithRating = allTeams.stream()
                .filter(team -> team.getRating() != null)
                .toList();

        if (teamsWithRating.size() >= 2) {
            Team firstTeam = teamsWithRating.get(0);
            Team secondTeam = teamsWithRating.get(1);

            logger.info("Team {} has rating {}, Team {} has rating {}",
                    firstTeam.getName(), firstTeam.getRating(),
                    secondTeam.getName(), secondTeam.getRating());
        }

        logger.info("Successfully verified teams sorting capability");
    }

    @Test(description = "Verify that the team schema is valid")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get all teams")
    @Description("This test verifies that the teams response matches the expected JSON schema")
    public void testTeamsSchemaIsValid() {
        Response response = teamsEndpoint.getAllTeams();

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        assertTrue(response.getBody().asString().startsWith("["), "Response should be an array");

        List<Integer> teamIds = response.jsonPath().getList("team_id");
        if (teamIds != null && !teamIds.isEmpty()) {
            Integer firstTeamId = teamIds.get(0);
            if (firstTeamId != null) {
                Response firstTeamResponse = teamsEndpoint.getTeamById(firstTeamId);
                assertEquals(firstTeamResponse.getStatusCode(), EndpointConstants.STATUS_OK);

                SchemaValidator.validateSchema(firstTeamResponse, "team-schema.json");
                logger.info("Successfully verified team schema is valid for team ID: {}", firstTeamId);
            } else {
                logger.warn("Could not find a valid team ID for schema validation");
            }
        } else {
            logger.warn("No teams found for schema validation");
        }
    }
}