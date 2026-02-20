package com.umc.ourcampus.review.domain;

import java.util.List;

public interface HashTagRepositoryCustom {
    List<HashTag> findTopHashTags(int size);
}
