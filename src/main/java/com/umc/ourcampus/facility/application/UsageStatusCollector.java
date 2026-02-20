package com.umc.ourcampus.facility.application;

import com.umc.ourcampus.facility.domain.UsageStatus;
import com.umc.ourcampus.university.domain.University;
import java.util.List;

public interface UsageStatusCollector {

    List<UsageStatus> collect();

    boolean supports(University university);
}
