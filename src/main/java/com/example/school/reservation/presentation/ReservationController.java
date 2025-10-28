package com.example.school.reservation.presentation;

import com.example.school.facility.converter.FacilityConverter;
import com.example.school.facility.domain.Facility;
import com.example.school.facility.application.dto.FacilityResponseDTO;
import com.example.school.global.apiPayload.ApiResponse;
import com.example.school.global.awsS3.AwsS3Service;
import com.example.school.reservation.application.ImageService;
import com.example.school.reservation.application.ReservationService;
import com.example.school.reservation.converter.ImageConverter;
import com.example.school.reservation.converter.ReservationConverter;
import com.example.school.reservation.domain.Image;
import com.example.school.reservation.domain.Reservation;
import com.example.school.reservation.application.dto.ImageResponseDTO;
import com.example.school.reservation.application.dto.ReservationRequestDTO;
import com.example.school.reservation.application.dto.ReservationResponseDTO;
import com.example.school.user.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservation")
public class ReservationController {
    private final ReservationService reservationService;
    private final AwsS3Service awsS3Service;
    private final ImageService imageService;

    // 예약하기
    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Reservation> reserve(@RequestBody ReservationRequestDTO.ReservationDTO reservationDTO,
                                            Authentication authentication) {
        boolean authenticated = authentication.isAuthenticated();
        System.out.println("인증 되었는가?" + authenticated);
        try {
            Reservation reservation = reservationService.createReservation(reservationDTO);

            return ApiResponse.onSuccess(reservation);
        } catch (RuntimeException e) {
            return ApiResponse.onFailure("COMMON400", e.getMessage());
        }
    }

    //예약 내역 확인(사용자 기준)
    @GetMapping("/details")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<ReservationResponseDTO.DetailResultDTO> getReservation(
            @RequestParam(name = "memberId") Long memberId, @RequestParam(name = "page") Integer page) {
        Page<Reservation> reservationList = reservationService.getReservation(memberId, page);
        return ApiResponse.onSuccess(ReservationConverter.detailResultListDTO(reservationList));
    }

    //예약 내역(시설물 기준)
    @GetMapping("/byfacility")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<ReservationResponseDTO.DetailResultDTO> getReservations(
            @RequestParam(name = "facilityId") Long facilityId, @RequestParam(name = "page") Integer page) {
        Page<Reservation> reservationList = reservationService.getReservationByFacilityId(facilityId, page);
        return ApiResponse.onSuccess(ReservationConverter.detailResultListDTO(reservationList));
    }

    //예약 불가능한 시간대
    @GetMapping("/time")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<ReservationResponseDTO.bookedUpListDTO> checkTime(
            @RequestParam(name = "facilityId") Long facilityId,
            @RequestParam(name = "year") String year, @RequestParam(name = "month") String month,
            @RequestParam(name = "day") String day) {
        List<Reservation> reservations = reservationService.possible_time(facilityId, year, month, day);
        return ApiResponse.onSuccess(ReservationConverter.bookedUpListDTO(reservations));
    }

    //예약 연장하기
    @PostMapping("/extend")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<ReservationResponseDTO.DetailDTO> extendTime(
            @RequestBody ReservationRequestDTO.ExtendDTO extendDTO) {
        try {
            Reservation reservation = reservationService.extendTime(extendDTO.getReservation_id(),
                    extendDTO.getExtendTime());
            return ApiResponse.onSuccess(ReservationConverter.detailResultDTO(reservation));
        } catch (RuntimeException e) {
            // RuntimeException이 발생한 경우에 대한 처리
            return ApiResponse.onFailure("COMMON400", e.getMessage()); // 적절한 에러 코드 및 메시지 설정
        }
    }

    //사용자 예약현황을 통해 이용한 시설물 목록 보기
    @GetMapping("/facility")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<FacilityResponseDTO.DetailResultDTO> useFacility(@RequestParam(name = "memberId") Long memberId,
                                                                        Integer page) {
        Page<Facility> facilities = reservationService.getFacilities(memberId, page);
        List<Reservation> reservationNo = reservationService.getReservation_no(memberId);
        Page<Reservation> useReservations = reservationService.useReservation(reservationNo, page);

        FacilityResponseDTO.DetailResultDTO detailResultDTO = FacilityConverter.detailResultDTO(facilities,
                useReservations);
        return ApiResponse.onSuccess(detailResultDTO);
    }

    //반납하기
    @PostMapping(value = "/return", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<ReservationResponseDTO.returnDTO> returnReservation(
            @RequestPart(value = "image", required = false) List<MultipartFile> imgFile,
            @RequestPart(value = "returnDTO") ReservationRequestDTO.returnDTO returnDTO) {
        log.info("이미지 : {}", imgFile);
        Reservation reservation = reservationService.getReservationById(returnDTO.getReservationId());
        reservationService.returnReservation(reservation);

        List<String> urls = awsS3Service.uploadFile(imgFile);
        urls.forEach(url -> {
            imageService.save(url, reservation);
        });
        ReservationResponseDTO.returnDTO returnDTO1 = ReservationConverter.returnReservation(reservation);

        return ApiResponse.onSuccess(returnDTO1);
    }

    //반납 인증 사진 url 조회
    @GetMapping("return/image/{reservationId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<ImageResponseDTO.ImageDTO> imgUrl(@PathVariable(name = "reservationId") Long reservationId) {
        Reservation reservation = reservationService.getReservationById(reservationId);
        List<Image> images = reservation.getImages();
        System.out.println("이미지?" + images);

        ImageResponseDTO.ImageDTO imageDTO = ImageConverter.imageDTO(images);
        return ApiResponse.onSuccess(imageDTO);
    }

    @GetMapping("/in-use")
    public ApiResponse<ReservationResponseDTO.InUse> getInUse(Authentication auth) {
        Member member = (Member) auth.getPrincipal();
        ReservationResponseDTO.InUse res = reservationService.getInUse(member.getId());

        return ApiResponse.onSuccess(res);
    }
}
