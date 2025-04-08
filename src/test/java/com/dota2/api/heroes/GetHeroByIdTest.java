package com.dota2.api.heroes;

import com.dota2.api.endpoints.HeroesEndpoint;
import com.dota2.config.TestConfig;
import com.dota2.constants.EndpointConstants;
import com.dota2.model.Hero;
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
 * Tests for the Hero by ID API endpoints
 */
@Epic("Dota 2 API Testing")
@Feature("Heroes API - Get By ID")
@Listeners(TestConfig.class)
public class GetHeroByIdTest {
    private static final Logger logger = LogManager.getLogger(GetHeroByIdTest.class);
    private HeroesEndpoint heroesEndpoint;

    private static final int VALID_HERO_ID_1 = 1;
    private static final int VALID_HERO_ID_2 = 5;
    private static final int VALID_HERO_ID_3 = 8;

    private static final int INVALID_HERO_ID = 999999;

    @BeforeClass
    public void setup() {
        heroesEndpoint = new HeroesEndpoint();
    }

    @DataProvider(name = "validHeroIds")
    public Object[][] validHeroIds() {
        return new Object[][] {
                { VALID_HERO_ID_1 },
                { VALID_HERO_ID_2 },
                { VALID_HERO_ID_3 }
        };
    }

    @Test(description = "Verify that getting a hero by valid ID returns 200 OK",
            dataProvider = "validHeroIds")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get hero by ID")
    @Description("This test verifies that getting a hero by a valid ID returns a 200 OK status code")
    public void testGetHeroByValidIdReturns200(int heroId) {
        Response response = heroesEndpoint.getHeroById(heroId);

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK,
                "Expected 200 OK status code for valid hero ID: " + heroId);

        response.then().body(EndpointConstants.ID_FIELD, equalTo(heroId));

        logger.info("Successfully verified GET /heroes/{} returns 200 OK with hero data", heroId);
    }

    @Test(description = "Verify that getting a hero by valid ID returns the correct hero data")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get hero by ID")
    @Description("This test verifies that getting a hero by ID returns the correct hero data")
    public void testGetHeroByIdReturnsCorrectData() {
        int heroId = VALID_HERO_ID_1; // Anti-Mage

        Response response = heroesEndpoint.getHeroById(heroId);

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        Hero hero = response.as(Hero.class);

        assertEquals(hero.getId().intValue(), heroId, "Hero ID should match the requested ID");
        assertNotNull(hero.getName(), "Hero name should not be null");
        assertNotNull(hero.getLocalizedName(), "Hero localized name should not be null");
        assertNotNull(hero.getPrimaryAttr(), "Hero primary attribute should not be null");
        assertNotNull(hero.getRoles(), "Hero roles should not be null");
        assertFalse(hero.getRoles().isEmpty(), "Hero should have at least one role");

        logger.info("Successfully verified hero data for ID: {}", heroId);
    }

    @Test(description = "Verify that getting a hero by invalid ID returns 404 Not Found")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get hero by ID")
    @Description("This test verifies that getting a hero by an invalid ID returns a 404 Not Found status code")
    public void testGetHeroByInvalidIdReturns404() {
        Response response = heroesEndpoint.getHeroById(INVALID_HERO_ID);

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_NOT_FOUND,
                "Expected 404 Not Found status code for invalid hero ID: " + INVALID_HERO_ID);

        logger.info("Successfully verified GET /heroes/{} returns 404 Not Found for invalid ID", INVALID_HERO_ID);
    }

    @Test(description = "Verify that hero by ID schema is valid")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get hero by ID")
    @Description("This test verifies that the hero by ID response matches the expected JSON schema")
    public void testHeroByIdSchemaIsValid() {
        Response response = heroesEndpoint.getHeroById(VALID_HERO_ID_1);

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        SchemaValidator.validateSchema(response, "hero-schema.json");

        logger.info("Successfully verified hero by ID schema is valid");
    }
}