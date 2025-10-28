package com.example.school.reservation.application;

import com.example.school.facility.application.FacilityService;
import com.example.school.facility.domain.Facility;
import com.example.school.global.apiPayload.GeneralException;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.reservation.converter.ReservationConverter;
import com.example.school.reservation.domain.Reservation;
import com.example.school.reservation.application.dto.ReservationRequestDTO;
import com.example.school.reservation.application.dto.ReservationResponseDTO;
import com.example.school.reservation.domain.ReservationRepository;
import com.example.school.user.application.UserService;
import com.example.school.user.domain.AlertType;
import com.example.school.user.domain.Member;
import com.example.school.user.domain.UserRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserService userService;

    private final FacilityService facilityService;
    private final UserRepository userRepository;
    @Autowired
    private final SimpMessagingTemplate template;


    //예약기능
    @Transactional
    public Reservation createReservation(ReservationRequestDTO.ReservationDTO reservationDTO) {
        String year = reservationDTO.getYear();
        String month = reservationDTO.getMonth();
        String day = reservationDTO.getDay();
        Long facilityId = reservationDTO.getFacilityId();
        List<Reservation> reservations = reservationRepository.findAllByFacilityIdAndYearAndMonthAndDay(facilityId,
                year, month, day);
        boolean isAllowed = reservations.stream()
                .allMatch(reservation -> isOverlap(reservation, reservationDTO.getStartTime(),
                        reservationDTO.getEndTime()));

        if (!isAllowed) {
            throw new RuntimeException("해당 시간대에는 이미 예약 되어 있습니다.");
        }

        Reservation reservation = ReservationConverter.reservation(reservationDTO);
        Member member = userService.findById(reservationDTO.getMemberId());
        Facility facility = facilityService.findById(reservationDTO.getFacilityId());
        reservation.setMember(member);
        reservation.setFacility(facility);

        Reservation savedReservation = reservationRepository.save(reservation);

        scheduleAlerts(savedReservation);

        return savedReservation;
    }

    // Schedule alerts for the reservation
    public void scheduleAlerts(Reservation reservation) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
        log.info("스케줄시작 알림: {}", reservation.getAlerts());

        AlertType[] alertTypes = AlertType.values();
        AlertType lastAlert = alertTypes[alertTypes.length - 1];

        for (AlertType alert : reservation.getAlerts()) {
            long timeUntilAlert = calculateTimeUntilAlert(reservation, alert);

            if (timeUntilAlert > 0) {
                log.info("예약 ID {}에 대한 {} 알림 전송 대기중", reservation.getId(), alert.name());
                scheduler.schedule(() -> sendAlert(reservation, alert), timeUntilAlert, TimeUnit.MILLISECONDS);
            }

            if (alert == lastAlert && timeUntilAlert == 1) {
                // 마감 시간이 되면 알림 전송
                String reviewMessage = "예약 ID " + reservation.getId() + "에 대한 이용한 시설물에 대한 리뷰를 달아주세요.";
                template.convertAndSend("/topic/alert", reviewMessage);
            }
        }
        scheduler.shutdown();
    }

    // 특정 알림까지의 시간 계산
    private long calculateTimeUntilAlert(Reservation reservation, AlertType alert) {
        LocalDateTime reservationEndTime = LocalDateTime.of(
                Integer.parseInt(reservation.getYear()),
                Integer.parseInt(reservation.getMonth()),
                Integer.parseInt(reservation.getDay()),
                (int) (reservation.getEnd_time() * 60 / 60),
                (int) (reservation.getEnd_time() * 60 % 60)
        );
        //알림을 예약할 시간(마감시간-원하는 n분전 알림시간)
        LocalDateTime alertTime = reservationEndTime.minus(alert.getMinutesBefore(), ChronoUnit.MINUTES);

        return Duration.between(LocalDateTime.now(), alertTime).toMillis();
    }

    // 알림 전송
    private void sendAlert(Reservation reservation, AlertType alert) {
        String alertMessage;

        switch (alert) {
            case THREE_DAYS_BEFORE:
                alertMessage = "예약 ID " + reservation.getId() + "에 대한 시설 이용 3일 전 안내";
                break;
            case ONE_DAY_BEFORE:
                alertMessage = "예약 ID " + reservation.getId() + "에 대한 시설 이용 1일 전 안내";
                break;
            case THIRTY_MINUTES_BEFORE:
                alertMessage = "예약 ID " + reservation.getId() + "에 대한 시설 반납 30분전 안내";
                break;
            case TEN_MINUTES_BEFORE:
                alertMessage = "예약 ID " + reservation.getId() + "에 대한 시설 반납 10분 전 안내";
                break;
            default:
                alertMessage = "예약 ID " + reservation.getId() + "에 대한 알림";
        }

        template.convertAndSend("/topic/alert", alertMessage);
        log.info("{} 전송 완료", alertMessage);

    }

    //반납하기
    @Transactional
    public Reservation returnReservation(Reservation reservation) {
        reservation.setBack(true);
        return reservationRepository.save(reservation);
    }

    //예약 연장
    @Transactional
    public Reservation extendTime(Long reservationId, Integer extendTime) {
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);

        if (reservation != null) {
            int newEndTime = reservation.getEnd_time() + extendTime;

            Long facilityId = reservation.getFacility().getId();
            String year = reservation.getYear();
            String month = reservation.getMonth();
            String day = reservation.getDay();
            List<Reservation> otherReservations = reservationRepository.findAllByFacilityIdAndYearAndMonthAndDay(
                            facilityId, year, month, day)
                    .stream()
                    .filter(otherReservation -> !otherReservation.getId().equals(reservation.getId()))  // 현재 예약을 제외
                    .filter(otherReservation -> otherReservation.getStart_time()
                            > reservation.getStart_time())  // 시작 시간이 newEndTime보다 큰 경우 필터링
                    .collect(Collectors.toList());
            // 다른 예약들의 startTime과 비교하여 연장이 가능한지 확인
            boolean isExtensionAllowed = otherReservations.stream()
                    .allMatch(otherReservation -> newEndTime <= otherReservation.getStart_time());

            if (isExtensionAllowed) {
                int newDuration = reservation.getDuration() + extendTime;
                // 연장 가능한 경우, endTime 업데이트
                reservation.setEnd_time(newEndTime);
                reservation.setDuration(newDuration);
                return reservationRepository.save(reservation);
            } else {
                // 연장이 불가능한 경우에 대한 처리
                throw new RuntimeException("예약 연장이 불가능합니다. 다른 예약과 시간이 겹칩니다.");
            }
        } else {
            // 예약이 존재하지 않는 경우에 대한 처리
            throw new RuntimeException("예약이 존재하지 않습니다.");
        }
    }

    // 두 예약의 시간 범위가 겹치는지 확인하는 메서드
    private boolean isOverlap(Reservation reservation, Integer newStartTime, Integer newEndTime) {
        Integer startTime = reservation.getStart_time();
        Integer endTime = reservation.getEnd_time();

        //겹치지 않는 경우
        if ((newStartTime <= startTime && newEndTime <= startTime) || (newStartTime >= endTime
                && newEndTime >= endTime)) {
            return true;
        } else {
            return false;
        }
    }

    //예약 불가능한 시간대
    public List<Reservation> possible_time(Long facilityId, String year, String month, String day) {
        return reservationRepository.findAllByFacilityIdAndYearAndMonthAndDay(facilityId, year, month, day);

    }


    //예약내역(페이지 있는 버전)
    public Page<Reservation> getReservation(Long memberId, Integer page) {
        return reservationRepository.findAllByMemberId(memberId, PageRequest.of(page - 1, 10));
    }

    //예약 내역(페이지 없는 버전)
    public List<Reservation> getReservation_no(Long memberId) {
        return reservationRepository.findAllByMemberId(memberId);
    }

    //예약 내역을 통해 이용한 시설물 추출
    public Page<Facility> getFacilities(Long memberId, Integer page) {
        Page<Reservation> reservations = reservationRepository.findAllByMemberId(memberId,
                PageRequest.of(page - 1, 10));
        return reservations.map(reservation -> reservation.getFacility());
    }

    //예약 아이디로 예약 찾기
    public Reservation getReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).get();
        return reservation;
    }

    //시설물 기준 예약 현황 목록
    public Page<Reservation> getReservationByFacilityId(Long facilityId, Integer page) {
        Page<Reservation> reservations = reservationRepository.findAllByFacilityId(facilityId,
                PageRequest.of(page - 1, 10));
        return reservations;
    }

    // 오늘 기준 이전 예약 추출
    public Page<Reservation> useReservation(List<Reservation> reservations, Integer page) {
        LocalDate today = LocalDate.now();

        List<Reservation> previousReservations = reservations.stream()
                .filter(reservation ->
                        Integer.parseInt(reservation.getYear()) < today.getYear()
                                || (Integer.parseInt(reservation.getYear()) == today.getYear()
                                && Integer.parseInt(reservation.getMonth()) < today.getMonthValue())
                                || (Integer.parseInt(reservation.getYear()) == today.getYear()
                                && Integer.parseInt(reservation.getMonth()) == today.getMonthValue()
                                && Integer.parseInt(reservation.getDay()) < today.getDayOfMonth())
                                || (Integer.parseInt(reservation.getYear()) == today.getYear()
                                && Integer.parseInt(reservation.getMonth()) == today.getMonthValue()
                                && Integer.parseInt(reservation.getDay()) == today.getDayOfMonth()
                                && reservation.getStart_time() < today.atStartOfDay().getHour()))
                .collect(Collectors.toList());

        int pageSize = 10;  // 페이지당 항목 수
        int start = (page - 1) * pageSize;
        int end = Math.min(page * pageSize, previousReservations.size());
        List<Reservation> pageReservations = previousReservations.subList(start, end);

        return new PageImpl<>(pageReservations, PageRequest.of(page - 1, pageSize), previousReservations.size());

    }

    public ReservationResponseDTO.InUse getInUse(Long id) {
        Member member = userRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();
        String year = Integer.toString(now.getYear());
        String month = Integer.toString(now.getMonthValue());
        String day = Integer.toString(now.getDayOfMonth());
        int hour = now.getHour();
        int minute = now.getMinute();

        Reservation entity = reservationRepository.findInUse(member, year, month, day, hour)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NO_CONTENT));

        int leftHour = entity.getEnd_time() - 1 - hour;
        int leftMinute = 60 - minute;
        String remainingTime = leftHour + ":" + leftMinute;

        return new ReservationResponseDTO.InUse(entity.getId(), entity.getFacility().getName(), entity.getEnd_time(),
                remainingTime);
    }
}