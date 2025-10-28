package com.example.school.facility.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class SearchLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    private Long count;

    @ManyToOne
    private School school;

    public void plusCount(){
        count+=1;
    }
}