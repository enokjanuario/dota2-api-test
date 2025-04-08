package com.dota2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Match {
    // Correção aqui - usando a mesma propriedade para ambos
    @JsonProperty("match_id")
    private Long matchId;

    @JsonProperty("start_time")
    private Long startTime;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("radiant_win")
    private Boolean radiantWin;

    @JsonProperty("leagueid")
    private Integer leagueId;

    @JsonProperty("game_mode")
    private Integer gameMode;

    @JsonProperty("radiant_team_id")
    private Integer radiantTeamId;

    @JsonProperty("dire_team_id")
    private Integer direTeamId;

    @JsonProperty("players")
    private List<MatchPlayer> players;

    @JsonProperty("lobby_type")
    private Integer lobbyType;

    @JsonProperty("cluster")
    private Integer cluster;

    @JsonProperty("replay_salt")
    private Long replaySalt;

    @JsonProperty("series_id")
    private Integer seriesId;

    @JsonProperty("match_seq_num")
    private Long matchSequenceNumber;

    @JsonProperty("series_type")
    private Integer seriesType;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MatchPlayer {
        @JsonProperty("account_id")
        private Long accountId;

        @JsonProperty("player_slot")
        private Integer playerSlot;

        @JsonProperty("hero_id")
        private Integer heroId;

        @JsonProperty("kills")
        private Integer kills;

        @JsonProperty("deaths")
        private Integer deaths;

        @JsonProperty("assists")
        private Integer assists;

        @JsonProperty("leaver_status")
        private Integer leaverStatus;

        @JsonProperty("gold")
        private Integer gold;

        @JsonProperty("last_hits")
        private Integer lastHits;

        @JsonProperty("denies")
        private Integer denies;

        @JsonProperty("gold_per_min")
        private Integer goldPerMin;

        @JsonProperty("xp_per_min")
        private Integer xpPerMin;

        @JsonProperty("level")
        private Integer level;
    }
}