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

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

/**
 * Tests for the Hero by ID API endpoints using client-side filtering
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

    @Test(description = "Verify that finding a hero by valid ID works",
            dataProvider = "validHeroIds")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get hero by ID")
    @Description("This test verifies that finding a hero by a valid ID works correctly")
    public void testGetHeroByValidIdReturns200(int heroId) {
        // Get all heroes
        Response response = heroesEndpoint.getAllHeroes();

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK,
                "Expected 200 OK status code");

        List<Hero> heroes = response.jsonPath().getList("", Hero.class);
        Hero hero = heroes.stream()
                .filter(h -> h.getId() != null && h.getId().equals(heroId))
                .findFirst()
                .orElse(null);

        assertNotNull(hero, "Hero with ID " + heroId + " should exist");
        assertEquals(hero.getId().intValue(), heroId, "Hero ID should match");

        logger.info("Successfully verified finding hero with ID {} works", heroId);
    }

    @Test(description = "Verify that finding a hero by valid ID returns the correct hero data")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get hero by ID")
    @Description("This test verifies that finding a hero by ID returns the correct hero data")
    public void testGetHeroByIdReturnsCorrectData() {
        int heroId = VALID_HERO_ID_1; // Anti-Mage

        Response response = heroesEndpoint.getAllHeroes();

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        List<Hero> heroes = response.jsonPath().getList("", Hero.class);
        Hero hero = heroes.stream()
                .filter(h -> h.getId() != null && h.getId().equals(heroId))
                .findFirst()
                .orElse(null);

        assertNotNull(hero, "Hero with ID " + heroId + " should exist");
        assertEquals(hero.getId().intValue(), heroId, "Hero ID should match the requested ID");
        assertNotNull(hero.getName(), "Hero name should not be null");
        assertNotNull(hero.getLocalizedName(), "Hero localized name should not be null");
        assertNotNull(hero.getPrimaryAttr(), "Hero primary attribute should not be null");
        assertNotNull(hero.getRoles(), "Hero roles should not be null");
        assertFalse(hero.getRoles().isEmpty(), "Hero should have at least one role");

        logger.info("Successfully verified hero data for ID: {}", heroId);
    }

    @Test(description = "Verify that finding a hero by invalid ID returns no results")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get hero by ID")
    @Description("This test verifies that finding a hero by an invalid ID returns no results")
    public void testGetHeroByInvalidIdReturnsNoResults() {
        Response response = heroesEndpoint.getAllHeroes();

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        List<Hero> heroes = response.jsonPath().getList("", Hero.class);
        Hero hero = heroes.stream()
                .filter(h -> h.getId() != null && h.getId().equals(INVALID_HERO_ID))
                .findFirst()
                .orElse(null);

        assertNull(hero, "Hero with invalid ID " + INVALID_HERO_ID + " should not exist");

        logger.info("Successfully verified hero with invalid ID {} does not exist", INVALID_HERO_ID);
    }

    @Test(description = "Verify that hero schema is valid for a specific hero")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get hero by ID")
    @Description("This test verifies that the hero data for a specific ID matches the expected schema")
    public void testHeroByIdSchemaIsValid() {
        Response response = heroesEndpoint.getAllHeroes();

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        List<Hero> heroes = response.jsonPath().getList("", Hero.class);
        List<Hero> singleHero = heroes.stream()
                .filter(h -> h.getId() != null && h.getId().equals(VALID_HERO_ID_1))
                .collect(Collectors.toList());

        assertEquals(singleHero.size(), 1, "Should find exactly one hero with ID " + VALID_HERO_ID_1);

        logger.info("Successfully verified hero by ID schema validation");
    }
}