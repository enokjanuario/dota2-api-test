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
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

/**
 * Tests for the Heroes API - GET endpoints
 */
@Epic("Dota 2 API Testing")
@Feature("Heroes API")
@Listeners(TestConfig.class)
public class GetHeroesTest {
    private static final Logger logger = LogManager.getLogger(GetHeroesTest.class);
    private HeroesEndpoint heroesEndpoint;

    @BeforeClass
    public void setup() {
        heroesEndpoint = new HeroesEndpoint();
    }

    @Test(description = "Verify that the GET /heroes endpoint returns 200 OK")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get all heroes")
    @Description("This test verifies that the GET /heroes endpoint returns a 200 OK status code")
    public void testGetAllHeroesReturns200() {
        Response response = heroesEndpoint.getAllHeroes();

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK,
                "Expected 200 OK status code");

        response.then().body("size()", greaterThan(0));

        logger.info("Successfully verified GET /heroes returns 200 OK with heroes data");
    }

    @Test(description = "Verify that the heroes data contains required fields")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get all heroes")
    @Description("This test verifies that each hero in the response contains required fields")
    public void testHeroesContainRequiredFields() {
        Response response = heroesEndpoint.getAllHeroes();

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        response.then().body("every { it.containsKey('" + EndpointConstants.ID_FIELD + "') }", is(true));
        response.then().body("every { it.containsKey('" + EndpointConstants.NAME_FIELD + "') }", is(true));
        response.then().body("every { it.containsKey('" + EndpointConstants.LOCALIZED_NAME_FIELD + "') }", is(true));
        response.then().body("every { it.containsKey('" + EndpointConstants.PRIMARY_ATTR_FIELD + "') }", is(true));
        response.then().body("every { it.containsKey('" + EndpointConstants.ROLES_FIELD + "') }", is(true));

        logger.info("Successfully verified heroes contain all required fields");
    }

    @Test(description = "Verify that the heroes schema is valid")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get all heroes")
    @Description("This test verifies that the heroes response matches the expected JSON schema")
    public void testHeroesSchemaIsValid() {
        Response response = heroesEndpoint.getAllHeroes();

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        SchemaValidator.validateSchema(response, "hero-schema.json");

        logger.info("Successfully verified heroes schema is valid");
    }

    @Test(description = "Verify that we can process heroes with pagination")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get heroes with pagination")
    @Description("This test verifies client-side pagination with limit and offset parameters")
    public void testHeroesPagination() {
        int limit = 5;
        int offset = 0;

        Response response = heroesEndpoint.getAllHeroes();

        assertEquals(response.getStatusCode(), EndpointConstants.STATUS_OK);

        List<Hero> allHeroes = response.jsonPath().getList("", Hero.class);
        List<Hero> firstPage = allHeroes.subList(offset, Math.min(offset + limit, allHeroes.size()));

        assertEquals(firstPage.size(), limit, "First page should contain " + limit + " heroes");

        offset = limit;
        List<Hero> secondPage = allHeroes.subList(offset, Math.min(offset + limit, allHeroes.size()));

        assertEquals(secondPage.size(), limit, "Second page should contain " + limit + " heroes");

        boolean hasDifferentHeroes = false;
        for (Hero hero1 : firstPage) {
            for (Hero hero2 : secondPage) {
                if (!hero1.getId().equals(hero2.getId())) {
                    hasDifferentHeroes = true;
                    break;
                }
            }
            if (hasDifferentHeroes) {
                break;
            }
        }

        assertTrue(hasDifferentHeroes, "The second page should contain different heroes than the first page");

        logger.info("Successfully verified heroes pagination processing");
    }
}