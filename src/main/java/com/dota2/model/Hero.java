package com.dota2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Model class for Hero data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hero {
    private Integer id;
    private String name;

    @JsonProperty("localized_name")
    private String localizedName;

    @JsonProperty("primary_attr")
    private String primaryAttr;

    @JsonProperty("attack_type")
    private String attackType;

    private List<String> roles;

    @JsonProperty("base_health")
    private Integer baseHealth;

    @JsonProperty("base_mana")
    private Integer baseMana;

    @JsonProperty("base_armor")
    private Double baseArmor;

    @JsonProperty("base_attack_min")
    private Integer baseAttackMin;

    @JsonProperty("base_attack_max")
    private Integer baseAttackMax;

    @JsonProperty("move_speed")
    private Integer moveSpeed;
}