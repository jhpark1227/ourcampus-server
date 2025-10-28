package com.example.school.facility.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class SearchRank {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    private int ranking;

    @ManyToOne(fetch = FetchType.LAZY)
    private School school;
}
