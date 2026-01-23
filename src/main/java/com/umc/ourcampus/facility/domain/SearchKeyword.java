package com.umc.ourcampus.facility.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record SearchKeyword(
        @Column(name = "keyword") String value
) {

    public SearchKeyword(String value) {
        this.value = value.trim();
    }
}
