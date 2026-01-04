package com.example.school.review.domain;

import java.util.List;

public interface HashTagRepositoryCustom {
    List<HashTag> findTopHashTags(int size);
}
