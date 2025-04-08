package com.dota2.api.matches;

import com.dota2.api.endpoints.MatchesEndpoint;
import com.dota2.config.TestConfig;
import com.dota2.constants.EndpointConstants;
import com.dota2.model.Match;
import com.dota2.utils.SchemaValidator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

@Epic("Dota 2 API Testing")
@Feature("Matches API - Get By ID")
@Listeners(TestConfig.class)
public class GetMatchByIdTest {
    private static final Logger logger = LogManager.getLogger(GetMatchByIdTest.class);
    private MatchesEndpoint matchesEndpoint;

    private static final long VALID_MATCH_ID_1 = 7487603797L;
    private static final long INVALID_MATCH_ID = 999999999999L;

    @BeforeClass
    public void setup() {
        matchesEndpoint = new MatchesEndpoint();
    }

    @Test(description = "Verify that getting a match by valid ID returns 200 OK")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get match by ID")
    @Description("This test verifies that getting a match by a valid ID returns a 200 OK status code")
    public void testGetMatchByValidIdReturns200() {
        Response response = matchesEndpoint.getMatchById(VALID_MATCH_ID_1);

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK,
                "Expected 200 OK status code for valid match ID: " + VALID_MATCH_ID_1);

        response.then().body("match_id", equalTo(VALID_MATCH_ID_1));

        logger.info("Successfully verified GET /matches/{} returns 200 OK with match data", VALID_MATCH_ID_1);
    }

    @Test(description = "Verify that getting a match by valid ID returns the correct match data")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get match by ID")
    @Description("This test verifies that getting a match by ID returns the correct match data")
    public void testGetMatchByIdReturnsCorrectData() {
        Response response = matchesEndpoint.getMatchById(VALID_MATCH_ID_1);

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        Match match = response.as(Match.class);

        assertEquals(match.getMatchId(), VALID_MATCH_ID_1, "Match ID should match the requested ID");
        assertNotNull(match.getStartTime(), "Match start time should not be null");
        assertNotNull(match.getDuration(), "Match duration should not be null");
        assertNotNull(match.getRadiantWin(), "Radiant win flag should not be null");
        assertNotNull(match.getPlayers(), "Match players should not be null");
        assertFalse(match.getPlayers().isEmpty(), "Match should have at least one player");

        logger.info("Successfully verified match data for ID: {}", VALID_MATCH_ID_1);
    }

}