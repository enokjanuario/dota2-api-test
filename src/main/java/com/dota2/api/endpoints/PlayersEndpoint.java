package com.dota2.api.endpoints;

import com.dota2.api.client.RestClient;
import com.dota2.constants.EndpointConstants;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Endpoint class for Players API
 */
public class PlayersEndpoint {
    private static final Logger logger = LogManager.getLogger(PlayersEndpoint.class);
    private final RestClient restClient;

    // Constante para conversão do SteamID64 para AccountID do Dota 2
    private static final long STEAM_ID_CONVERTER = 76561197960265728L;

    public PlayersEndpoint() {
        this.restClient = new RestClient();
    }

    /**
     * Converte um SteamID64 para um AccountID do Dota 2
     * @param steamId64 steamID no formato longo
     * @return accountId para Dota 2 API
     */
    public static long convertSteamIDToAccountID(long steamId64) {
        // Formula: AccountID = SteamID64 - 76561197960265728
        return steamId64 - STEAM_ID_CONVERTER;
    }

    /**
     * Gets player information by account ID
     * @param accountId Player's account ID (pode ser SteamID64 ou AccountID)
     * @return Response object
     */
    public Response getPlayerById(long accountId) {
        // Se o ID for do formato SteamID64 (maior que o valor conversor), converte para AccountID
        long dota2AccountId = accountId;
        if (accountId > STEAM_ID_CONVERTER) {
            dota2AccountId = convertSteamIDToAccountID(accountId);
            logger.info("Converting SteamID64 {} to Dota 2 AccountID {}", accountId, dota2AccountId);
        }

        logger.info("Getting player with account ID: {}", dota2AccountId);
        String endpoint = EndpointConstants.PLAYER_BY_ID_ENDPOINT.replace("{id}", String.valueOf(dota2AccountId));
        return restClient.get(endpoint);
    }

    /**
     * Gets player's recent matches
     * @param accountId Player's account ID (pode ser SteamID64 ou AccountID)
     * @return Response object with recent matches
     */
    public Response getPlayerRecentMatches(long accountId) {
        // Se o ID for do formato SteamID64 (maior que o valor conversor), converte para AccountID
        long dota2AccountId = accountId;
        if (accountId > STEAM_ID_CONVERTER) {
            dota2AccountId = convertSteamIDToAccountID(accountId);
            logger.info("Converting SteamID64 {} to Dota 2 AccountID {}", accountId, dota2AccountId);
        }

        logger.info("Getting recent matches for player with account ID: {}", dota2AccountId);
        String endpoint = EndpointConstants.PLAYER_BY_ID_ENDPOINT.replace("{id}", String.valueOf(dota2AccountId)) + "/recentMatches";
        return restClient.get(endpoint);
    }

    /**
     * Gets player's win/loss count
     * @param accountId Player's account ID (pode ser SteamID64 ou AccountID)
     * @return Response object with win/loss data
     */
    public Response getPlayerWinLoss(long accountId) {
        // Se o ID for do formato SteamID64 (maior que o valor conversor), converte para AccountID
        long dota2AccountId = accountId;
        if (accountId > STEAM_ID_CONVERTER) {
            dota2AccountId = convertSteamIDToAccountID(accountId);
            logger.info("Converting SteamID64 {} to Dota 2 AccountID {}", accountId, dota2AccountId);
        }

        logger.info("Getting win/loss data for player with account ID: {}", dota2AccountId);
        String endpoint = EndpointConstants.PLAYER_BY_ID_ENDPOINT.replace("{id}", String.valueOf(dota2AccountId)) + "/wl";
        return restClient.get(endpoint);
    }

    /**
     * Gets player's matches with filters
     * @param accountId Player's account ID (pode ser SteamID64 ou AccountID)
     * @param parameters Map of query parameters for filtering
     * @return Response object with filtered matches
     */
    public Response getPlayerMatches(long accountId, Map<String, Object> parameters) {
        // Se o ID for do formato SteamID64 (maior que o valor conversor), converte para AccountID
        long dota2AccountId = accountId;
        if (accountId > STEAM_ID_CONVERTER) {
            dota2AccountId = convertSteamIDToAccountID(accountId);
            logger.info("Converting SteamID64 {} to Dota 2 AccountID {}", accountId, dota2AccountId);
        }

        logger.info("Getting matches for player with account ID: {} and parameters: {}", dota2AccountId, parameters);
        String endpoint = EndpointConstants.PLAYER_BY_ID_ENDPOINT.replace("{id}", String.valueOf(dota2AccountId)) + "/matches";
        return restClient.get(endpoint, parameters);
    }

    /**
     * Gets player's hero statistics
     * @param accountId Player's account ID (pode ser SteamID64 ou AccountID)
     * @return Response object with hero stats
     */
    public Response getPlayerHeroes(long accountId) {
        // Se o ID for do formato SteamID64 (maior que o valor conversor), converte para AccountID
        long dota2AccountId = accountId;
        if (accountId > STEAM_ID_CONVERTER) {
            dota2AccountId = convertSteamIDToAccountID(accountId);
            logger.info("Converting SteamID64 {} to Dota 2 AccountID {}", accountId, dota2AccountId);
        }

        logger.info("Getting hero statistics for player with account ID: {}", dota2AccountId);
        String endpoint = EndpointConstants.PLAYER_BY_ID_ENDPOINT.replace("{id}", String.valueOf(dota2AccountId)) + "/heroes";
        return restClient.get(endpoint);
    }

    /**
     * Gets player's peers (other players they've played with)
     * @param accountId Player's account ID (pode ser SteamID64 ou AccountID)
     * @return Response object with peers data
     */
    public Response getPlayerPeers(long accountId) {
        // Se o ID for do formato SteamID64 (maior que o valor conversor), converte para AccountID
        long dota2AccountId = accountId;
        if (accountId > STEAM_ID_CONVERTER) {
            dota2AccountId = convertSteamIDToAccountID(accountId);
            logger.info("Converting SteamID64 {} to Dota 2 AccountID {}", accountId, dota2AccountId);
        }

        logger.info("Getting peers for player with account ID: {}", dota2AccountId);
        String endpoint = EndpointConstants.PLAYER_BY_ID_ENDPOINT.replace("{id}", String.valueOf(dota2AccountId)) + "/peers";
        return restClient.get(endpoint);
    }
}