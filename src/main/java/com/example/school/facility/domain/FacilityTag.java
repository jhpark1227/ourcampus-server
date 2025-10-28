package com.example.school.facility.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum FacilityTag {
    QUIET("#조용한"), ACTIVE("#활기찬"), TEAMPLAY("#팀플"), CROWDED("#사람이 많은"), SECLUDED("#사람이 적은");

    private final String tag;
}
