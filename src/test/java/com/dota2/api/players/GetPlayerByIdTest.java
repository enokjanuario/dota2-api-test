package com.dota2.api.players;

import com.dota2.api.endpoints.PlayersEndpoint;
import com.dota2.config.TestConfig;
import com.dota2.constants.EndpointConstants;
import com.dota2.model.Player;
import com.dota2.utils.SchemaValidator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

/**
 * Tests for the Player by ID API endpoints
 */
@Epic("Dota 2 API Testing")
@Feature("Players API - Get By ID")
@Listeners(TestConfig.class)
public class GetPlayerByIdTest {
    private static final Logger logger = LogManager.getLogger(GetPlayerByIdTest.class);
    private PlayersEndpoint playersEndpoint;

    // Arteezy
    private static final long VALID_PLAYER_ID_1 = 86745912L;
    // Miracle-
    private static final long VALID_PLAYER_ID_2 = 105248644L;
    // Invalid player ID
    private static final long INVALID_PLAYER_ID = 9999999999L;

    @BeforeClass
    public void setup() {
        playersEndpoint = new PlayersEndpoint();
    }

    @DataProvider(name = "validPlayerIds")
    public Object[][] validPlayerIds() {
        return new Object[][] {
                { VALID_PLAYER_ID_1 },
                { VALID_PLAYER_ID_2 }
        };
    }

    @Test(description = "Verify that getting a player by valid ID returns 200 OK",
            dataProvider = "validPlayerIds")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get player by ID")
    @Description("This test verifies that getting a player by a valid ID returns a 200 OK status code")
    public void testGetPlayerByValidIdReturns200(long playerId) {
        Response response = playersEndpoint.getPlayerById(playerId);

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK,
                "Expected 200 OK status code for valid player ID: " + playerId);

        response.then().body("profile.account_id", notNullValue());

        logger.info("Successfully verified GET /players/{} returns 200 OK with player data", playerId);
    }

    @Test(description = "Verify that getting a player by valid ID returns the correct player data")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get player by ID")
    @Description("This test verifies that getting a player by ID returns the correct player data")
    public void testGetPlayerByIdReturnsCorrectData() {
        Response response = playersEndpoint.getPlayerById(VALID_PLAYER_ID_1);

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        Player player = response.as(Player.class);

        assertNotNull(player.getProfile(), "Player profile should not be null");
        assertNotNull(player.getProfile().getAccountId(), "Player profile account ID should not be null");
        assertEquals(player.getProfile().getAccountId().longValue(), VALID_PLAYER_ID_1,
                "Profile account ID should match the requested ID");

        assertNotNull(player.getEffectiveAccountId(), "Effective account ID should not be null");
        assertEquals(player.getEffectiveAccountId().longValue(), VALID_PLAYER_ID_1,
                "Effective account ID should match the requested ID");

        logger.info("Successfully verified player data for ID: {}", VALID_PLAYER_ID_1);
    }

    @Test(description = "Verify that player schema is valid")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get player by ID")
    @Description("This test verifies that the player data matches the expected schema")
    public void testPlayerSchemaIsValid() {
        Response response = playersEndpoint.getPlayerById(VALID_PLAYER_ID_1);

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        SchemaValidator.validateSchema(response, "player-schema.json");

        logger.info("Successfully verified player schema is valid");
    }

    @Test(description = "Verify that getting recent matches for a player works")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get player recent matches")
    @Description("This test verifies that getting recent matches for a player returns valid data")
    public void testGetPlayerRecentMatches() {
        Response response = playersEndpoint.getPlayerRecentMatches(VALID_PLAYER_ID_1);

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        String responseBody = response.getBody().asString();
        if (!responseBody.equals("[]") && !responseBody.equals("{}")) {
            response.then().body("size()", greaterThan(0));
            response.then().body("[0]", hasKey("match_id"));
        } else {
            logger.info("No recent matches found for player ID: {}, but API returned successfully", VALID_PLAYER_ID_1);
        }

        logger.info("Successfully verified recent matches for player ID: {}", VALID_PLAYER_ID_1);
    }

    @Test(description = "Verify that getting win/loss data for a player works")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get player win/loss data")
    @Description("This test verifies that getting win/loss statistics for a player returns valid data")
    public void testGetPlayerWinLoss() {
        Response response = playersEndpoint.getPlayerWinLoss(VALID_PLAYER_ID_1);

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);
        response.then().body("win", notNullValue());
        response.then().body("lose", notNullValue());

        response.then().body("win", greaterThanOrEqualTo(0));
        response.then().body("lose", greaterThanOrEqualTo(0));

        logger.info("Successfully verified win/loss data for player ID: {}", VALID_PLAYER_ID_1);
    }

    @Test(description = "Verify that getting a player by invalid ID returns appropriate response")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get player by invalid ID")
    @Description("This test verifies the behavior when requesting an invalid player ID")
    public void testGetPlayerByInvalidId() {
        Response response = playersEndpoint.getPlayerById(INVALID_PLAYER_ID);

        if (response.getStatusCode() == EndpointConstants.STATUS_NOT_FOUND) {
            logger.info("API correctly returned 404 for invalid player ID: {}", INVALID_PLAYER_ID);
        } else if (response.getStatusCode() == EndpointConstants.STATUS_OK) {
            String responseBody = response.getBody().asString();

            assertTrue(responseBody.equals("{}") ||
                            responseBody.equals("[]") ||
                            responseBody.contains("\"profile\":null") ||
                            !responseBody.contains("\"account_id\""),
                    "Response body should indicate invalid player");
            logger.info("API returned empty data for invalid player ID: {}", INVALID_PLAYER_ID);
        } else {
            fail("Unexpected status code: " + response.getStatusCode());
        }
    }
}