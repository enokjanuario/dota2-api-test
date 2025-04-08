package com.dota2.constants;

/**
 * Constants for API endpoints
 */
public class EndpointConstants {

    public static final String HEROES_ENDPOINT = "/heroes";
    public static final String HERO_BY_ID_ENDPOINT = "/heroes/{id}";

    public static final String MATCHES_ENDPOINT = "/matches";
    public static final String MATCH_BY_ID_ENDPOINT = "/matches/{id}";

    public static final String PLAYERS_ENDPOINT = "/players";
    public static final String PLAYER_BY_ID_ENDPOINT = "/players/{id}";

    public static final String TEAMS_ENDPOINT = "/teams";
    public static final String TEAM_BY_ID_ENDPOINT = "/teams/{id}";

    public static final String LIMIT_PARAM = "limit";
    public static final String OFFSET_PARAM = "offset";
    public static final String SORT_PARAM = "sort";

    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String LOCALIZED_NAME_FIELD = "localized_name";
    public static final String PRIMARY_ATTR_FIELD = "primary_attr";
    public static final String ATTACK_TYPE_FIELD = "attack_type";
    public static final String ROLES_FIELD = "roles";

    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATED = 201;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_SERVER_ERROR = 500;

    private EndpointConstants() {

    }
}