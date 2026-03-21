package com.umc.ourcampus.review.domain;

import java.util.List;

public interface HashTagRepositoryCustom {
    List<HashTag> findRandomHashTags(int size, long seed);
}
