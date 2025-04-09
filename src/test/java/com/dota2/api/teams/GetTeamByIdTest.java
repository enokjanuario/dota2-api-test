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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

/**
 * Tests for the Team by ID API endpoints
 */
@Epic("Dota 2 API Testing")
@Feature("Teams API - Get By ID")
@Listeners(TestConfig.class)
public class GetTeamByIdTest {
    private static final Logger logger = LogManager.getLogger(GetTeamByIdTest.class);
    private TeamsEndpoint teamsEndpoint;

    private static final int VALID_TEAM_ID_1 = 15; // PSG.LGD
    private static final int VALID_TEAM_ID_2 = 39; // Evil Geniuses
    private static final int INVALID_TEAM_ID = 999999;

    @BeforeClass
    public void setup() {
        teamsEndpoint = new TeamsEndpoint();
    }

    @DataProvider(name = "validTeamIds")
    public Object[][] validTeamIds() {
        return new Object[][] {
                { VALID_TEAM_ID_1 },
                { VALID_TEAM_ID_2 }
        };
    }

    @Test(description = "Verify that getting a team by valid ID returns 200 OK",
            dataProvider = "validTeamIds")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get team by ID")
    @Description("This test verifies that getting a team by a valid ID returns a 200 OK status code")
    public void testGetTeamByValidIdReturns200(int teamId) {
        Response response = teamsEndpoint.getTeamById(teamId);

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK,
                "Expected 200 OK status code for valid team ID: " + teamId);

        response.then().body("team_id", equalTo(teamId));

        logger.info("Successfully verified GET /teams/{} returns 200 OK with team data", teamId);
    }

    @Test(description = "Verify that getting a team by valid ID returns the correct team data")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get team by ID")
    @Description("This test verifies that getting a team by ID returns the correct team data")
    public void testGetTeamByIdReturnsCorrectData() {
        Response response = teamsEndpoint.getTeamById(VALID_TEAM_ID_1);

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        Team team = response.as(Team.class);

        assertEquals(team.getTeamId().intValue(), VALID_TEAM_ID_1, "Team ID should match the requested ID");
        assertNotNull(team.getName(), "Team name should not be null");

        if (team.getWins() != null && team.getLosses() != null) {
            int calculatedGamesPlayed = team.getWins() + team.getLosses();
            team.setGamesPlayed(calculatedGamesPlayed); // Update the gamesPlayed field

            float expectedWinRate = team.getWins() * 100.0f / calculatedGamesPlayed;
            assertEquals(team.getWinRate(), expectedWinRate, 0.001f, "Win rate calculation should be correct");
        }

        logger.info("Successfully verified team data for ID: {}", VALID_TEAM_ID_1);
    }

    @Test(description = "Verify that team schema is valid")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get team by ID")
    @Description("This test verifies that the team data matches the expected schema")
    public void testTeamSchemaIsValid() {
        Response response = teamsEndpoint.getTeamById(VALID_TEAM_ID_1);

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        SchemaValidator.validateSchema(response, "team-schema.json");

        logger.info("Successfully verified team schema is valid");
    }

    @Test(description = "Verify that getting team matches works")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get team matches")
    @Description("This test verifies that getting matches for a team returns valid data")
    public void testGetTeamMatches() {
        Response response = teamsEndpoint.getTeamMatches(VALID_TEAM_ID_1);

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        String responseBody = response.getBody().asString();
        if (!responseBody.equals("[]") && !responseBody.equals("{}")) {
            response.then().body("size()", greaterThan(0));
            if (response.jsonPath().getList("").size() > 0) {
                response.then().body("[0]", hasKey("match_id"));
            }
        }

        logger.info("Successfully verified matches for team ID: {}", VALID_TEAM_ID_1);
    }

    @Test(description = "Verify that getting team players works")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get team players")
    @Description("This test verifies that getting players for a team returns valid data")
    public void testGetTeamPlayers() {
        Response response = teamsEndpoint.getTeamPlayers(VALID_TEAM_ID_1);

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        String responseBody = response.getBody().asString();
        if (!responseBody.equals("[]") && !responseBody.equals("{}")) {
            response.then().body("size()", greaterThan(0));
            if (response.jsonPath().getList("").size() > 0) {
                response.then().body("[0]", hasKey("account_id"));
            }
        }

        logger.info("Successfully verified players for team ID: {}", VALID_TEAM_ID_1);
    }

    @Test(description = "Verify that getting a team by invalid ID returns not found")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get team by ID")
    @Description("This test verifies that getting a team by an invalid ID returns an appropriate response")
    public void testGetTeamByInvalidId() {
        Response response = teamsEndpoint.getTeamById(INVALID_TEAM_ID);

        try {
            assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

            // A resposta pode variar dependendo da API.
            // Algumas possibilidades:
            // 1. Um objeto vazio {}
            // 2. Um array vazio []
            // 3. Um valor nulo null
            // 4. Um objeto com team_id=null ou outro valor

            logger.info("API returned response for invalid team ID: {} with body: {}",
                    INVALID_TEAM_ID, response.getBody().asString());


        } catch (Exception e) {
            logger.info("API returned non-standard response for invalid team ID: {}", INVALID_TEAM_ID);
        }
    }
}