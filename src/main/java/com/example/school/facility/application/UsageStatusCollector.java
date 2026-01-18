package com.example.school.facility.application;

import com.example.school.facility.domain.UsageStatus;
import com.example.school.university.domain.University;
import java.util.List;

public interface UsageStatusCollector {

    List<UsageStatus> collect();

    boolean supports(University university);
}
