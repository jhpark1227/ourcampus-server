package com.example.school.user.application;

import com.example.school.review.domain.Review;
import com.example.school.user.domain.Member;
import com.example.school.user.dto.UserResponseDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface UserQueryService {

    Optional<Member> findMember(Long id);

    Page<UserResponseDTO.ReviewPreViewDTO> getReviewList(Long MemberId, Integer page);

    Page<UserResponseDTO.ReviewPreViewDTO> findByFacility(Long facilityId, Integer page);

    Page<UserResponseDTO.ReviewPreViewDTO> getAllReviewList(Integer page);

    Optional<Review> findById(Long id);

    UserResponseDTO.Info getInfo(Long id);
}