package com.dota2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class for Team data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {
    @JsonProperty("team_id")
    private Integer teamId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("tag")
    private String tag;

    @JsonProperty("logo_url")
    private String logoUrl;

    @JsonProperty("last_match_time")
    private Long lastMatchTime;

    @JsonProperty("rating")
    private Float rating;

    @JsonProperty("wins")
    private Integer wins;

    @JsonProperty("losses")
    private Integer losses;

    @Builder.Default
    @JsonProperty("games_played")
    private Integer gamesPlayed = 0;

    /**
     * Calculate win rate percentage
     * @return win rate as a percentage
     */
    public Float getWinRate() {
        if (gamesPlayed == null || gamesPlayed == 0) {
            return 0.0f;
        }
        return (wins == null ? 0 : wins) * 100.0f / gamesPlayed;
    }

    /**
     * Calculate if the team is considered active based on last match time
     * @return true if team had a match in the last 30 days
     */
    public boolean isActive() {
        if (lastMatchTime == null) {
            return false;
        }
        long thirtyDaysInSeconds = 30L * 24 * 60 * 60;
        long currentTime = System.currentTimeMillis() / 1000;
        return (currentTime - lastMatchTime) < thirtyDaysInSeconds;
    }
}