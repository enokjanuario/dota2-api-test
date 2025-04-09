package com.dota2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class for Player data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Player {

    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("personaname")
    private String personaName;

    @JsonProperty("name")
    private String name;

    @JsonProperty("plus")
    private Boolean plus;

    @JsonProperty("steamid")
    private String steamId;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("avatarmedium")
    private String avatarMedium;

    @JsonProperty("avatarfull")
    private String avatarFull;

    @JsonProperty("profileurl")
    private String profileUrl;

    @JsonProperty("last_login")
    private String lastLogin;

    @JsonProperty("loccountrycode")
    private String countryCode;

    @JsonProperty("is_contributor")
    private Boolean isContributor;

    private Integer win;
    private Integer lose;

    @JsonProperty("matches_played")
    private Integer matchesPlayed;

    @JsonProperty("solo_competitive_rank")
    private String soloCompetitiveRank;

    @JsonProperty("competitive_rank")
    private String competitiveRank;

    @JsonProperty("rank_tier")
    private Integer rankTier;

    @JsonProperty("leaderboard_rank")
    private Integer leaderboardRank;

    @JsonProperty("mmr_estimate")
    private MmrEstimate mmrEstimate;

    @JsonProperty("profile")
    private Profile profile;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Profile {
        @JsonProperty("account_id")
        private Long accountId;

        @JsonProperty("personaname")
        private String personaName;

        @JsonProperty("name")
        private String name;

        @JsonProperty("plus")
        private Boolean plus;

        @JsonProperty("steamid")
        private String steamId;

        @JsonProperty("avatar")
        private String avatar;

        @JsonProperty("avatarmedium")
        private String avatarMedium;

        @JsonProperty("avatarfull")
        private String avatarFull;

        @JsonProperty("profileurl")
        private String profileUrl;

        @JsonProperty("last_login")
        private String lastLogin;

        @JsonProperty("loccountrycode")
        private String countryCode;

        @JsonProperty("is_contributor")
        private Boolean isContributor;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MmrEstimate {
        private Integer estimate;
        @JsonProperty("stdDev")
        private Integer stdDev;
        @JsonProperty("n")
        private Integer n;
    }

    /**
     * @return o account ID do jogador
     */
    public Long getEffectiveAccountId() {
        if (accountId != null) {
            return accountId;
        } else if (profile != null && profile.getAccountId() != null) {
            return profile.getAccountId();
        }
        return null;
    }
}