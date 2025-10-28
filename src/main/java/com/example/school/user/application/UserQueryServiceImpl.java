package com.example.school.user.application;

import com.example.school.facility.domain.Facility;
import com.example.school.facility.domain.FacilityRepository;
import com.example.school.global.apiPayload.GeneralException;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.review.domain.Review;
import com.example.school.user.converter.UserConverter;
import com.example.school.user.domain.Member;
import com.example.school.user.dto.UserResponseDTO;
import com.example.school.user.domain.ReviewRepository;
import com.example.school.user.domain.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final FacilityRepository facilityRepository;

    @Override
    public Optional<Member> findMember(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Page<UserResponseDTO.ReviewPreViewDTO> getReviewList(Long MemberId, Integer page) {

        Member member = userRepository.findById(MemberId).get();

        Page<Review> memberreviewpage = reviewRepository.findAllByMember(member, PageRequest.of(page, 10));
        List<UserResponseDTO.ReviewPreViewDTO> reviewPreViewDTOList = memberreviewpage.stream()
                .map(UserConverter::reviewPreViewDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(reviewPreViewDTOList, memberreviewpage.getPageable(),
                memberreviewpage.getTotalElements());
    }

    @Override
    public Page<UserResponseDTO.ReviewPreViewDTO> findByFacility(Long facilityId, Integer page) {
        Facility facility = facilityRepository.findById(facilityId).get();
        Page<Review> facilityreviewpage = reviewRepository.findAllByFacility(facility, PageRequest.of(page - 1, 10));

        List<UserResponseDTO.ReviewPreViewDTO> reviewPreViewDTOList = facilityreviewpage.stream()
                .map(UserConverter::reviewPreViewDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(reviewPreViewDTOList, facilityreviewpage.getPageable(),
                facilityreviewpage.getTotalElements());

    }

    @Override
    public Page<UserResponseDTO.ReviewPreViewDTO> getAllReviewList(Integer page) {
        Page<Review> reviewPage = reviewRepository.findAll(PageRequest.of(page, 10));
        List<UserResponseDTO.ReviewPreViewDTO> reviewPreViewDTOList = reviewPage.stream()
                .map(UserConverter::reviewPreViewDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(reviewPreViewDTOList, reviewPage.getPageable(), reviewPage.getTotalElements());
    }

    @Override
    public Optional<Review> findById(Long id) {
        {
            return reviewRepository.findById(id);
        }
    }

    @Override
    public UserResponseDTO.Info getInfo(Long id) {
        Member member = userRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        return new UserResponseDTO.Info(member);
    }
}
