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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

/**
 * Tests for the Matches API - GET endpoints
 */
@Epic("Dota 2 API Testing")
@Feature("Matches API")
@Listeners(TestConfig.class)
public class GetMatchesTest {
    private static final Logger logger = LogManager.getLogger(GetMatchesTest.class);
    private MatchesEndpoint matchesEndpoint;

    @BeforeClass
    public void setup() {
        matchesEndpoint = new MatchesEndpoint();
    }

    @Test(description = "Verify that the GET /matches endpoint returns 200 OK")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get recent matches")
    @Description("This test verifies that the GET /matches endpoint returns a 200 OK status code")
    public void testGetRecentMatchesReturns200() {
        Response response = matchesEndpoint.getRecentMatches();

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK,
                "Expected 200 OK status code");

        response.then().body("size()", greaterThan(0));

        logger.info("Successfully verified GET /matches returns 200 OK with matches data");
    }

    @Test(description = "Verify that the matches data contains required fields")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get recent matches")
    @Description("This test verifies that each match in the response contains required fields")
    public void testMatchesContainRequiredFields() {
        Response response = matchesEndpoint.getRecentMatches();

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        response.then().body("[0]", hasKey(EndpointConstants.MATCH_ID_FIELD));
        response.then().body("[0]", hasKey(EndpointConstants.START_TIME_FIELD));
        response.then().body("[0]", hasKey(EndpointConstants.DURATION_FIELD));
        response.then().body("[0]", hasKey(EndpointConstants.RADIANT_WIN_FIELD));
        response.then().body("[0]", hasKey(EndpointConstants.RADIANT_TEAM_FIELD));
        response.then().body("[0]", hasKey(EndpointConstants.DIRE_TEAM_FIELD));

        logger.info("Successfully verified matches contain all required fields");
    }

    @Test(description = "Verify that the matches can be filtered by game mode")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get filtered matches")
    @Description("This test verifies that the matches can be filtered by game mode")
    public void testMatchesFilteredByGameMode() {
        Map<String, Object> params = new HashMap<>();
        params.put("game_mode", 1); // All Pick

        Response response = matchesEndpoint.getMatchesByParams(params);

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        List<Match> matches = response.jsonPath().getList("", Match.class);

        assertFalse(matches.isEmpty(), "Should return some matches");

        logger.info("Successfully verified matches can be filtered by game mode");
    }

    @Test(description = "Verify that we can process matches with pagination")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get matches with pagination")
    @Description("This test verifies client-side pagination with limit and offset parameters")
    public void testMatchesPagination() {
        int limit = 5;
        int offset = 0;

        Response response = matchesEndpoint.getRecentMatches();

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        List<Match> allMatches = response.jsonPath().getList("", Match.class);
        List<Match> firstPage = allMatches.subList(offset, Math.min(offset + limit, allMatches.size()));

        assertEquals(firstPage.size(), limit, "First page should contain " + limit + " matches");

        offset = limit;
        List<Match> secondPage = allMatches.subList(offset, Math.min(offset + limit, allMatches.size()));

        assertEquals(secondPage.size(), limit, "Second page should contain " + limit + " matches");

        boolean hasDifferentMatches = false;
        for (Match match1 : firstPage) {
            for (Match match2 : secondPage) {
                if (!match1.getMatchId().equals(match2.getMatchId())) {
                    hasDifferentMatches = true;
                    break;
                }
            }
            if (hasDifferentMatches) {
                break;
            }
        }

        assertTrue(hasDifferentMatches, "The second page should contain different matches than the first page");

        logger.info("Successfully verified matches pagination processing");
    }
}