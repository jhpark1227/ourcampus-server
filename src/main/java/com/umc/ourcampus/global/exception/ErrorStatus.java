package com.umc.ourcampus.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    PAGE_LT_ONE(HttpStatus.BAD_REQUEST, "잘못된 페이지입니다."),
    BAD_QUERY_STRING(HttpStatus.BAD_REQUEST, "잘못된 쿼리스트링입니다."),
    NO_CONTENT(HttpStatus.BAD_REQUEST, "결과가 존재하지 않습니다."),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "RefreshToken이 일치하지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.OK, "서버 에러"),
    BAD_JWT(HttpStatus.UNAUTHORIZED, "JWT 토큰이 잘못되었습니다."),

    // 멤버 관려 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용자가 없습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰가 없습니다."),

    INVALID_HEAD_COUNT(HttpStatus.BAD_REQUEST, "잘못된 예약 인원입니다."),
    TIMESLOT_OVERLAP(HttpStatus.BAD_REQUEST, "예약 시간이 겹칩니다."),
    INVALID_TIMESLOT(HttpStatus.BAD_REQUEST, "잘못된 예약 시간입니다."),
    TOO_LONG_TIMESLOT(HttpStatus.BAD_REQUEST, "예약 시간은 다음 날로 넘어갈 수 없습니다."),

    // 인증, 인가 관련 에러
    USER_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "사용 불가능한 아이디입니다."),
    NICKNAME_DUPLICATE(HttpStatus.BAD_REQUEST, "사용 불가능한 닉네임입니다."),
    PASSWORD_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "사용 불가능한 비밀번호입니다."),
    EMAIL_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "사용 불가능한 이메일입니다."),
    VERIFICATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "인증 정보가 존재하지 않습니다."),
    EMAIL_SEND_ERROR(HttpStatus.BAD_REQUEST, "이메일 전송에 실패했습니다."),
    INVALID_CURRENT_PASSWORD(HttpStatus.BAD_REQUEST, "현재 비밀번호 불일치"),
    USERID_MISMATCH(HttpStatus.BAD_REQUEST, "유저 아이디와 이메일이 일치하지 않습니다."),
    FIND_PASSWORD_ERROR(HttpStatus.BAD_REQUEST, "비밀번호 찾기 실패"),
    LOGIN_ERROR(HttpStatus.BAD_REQUEST, "로그인에 실패했습니다."),
    EMAIL_DUPLICATE(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    USERID_DUPLICATE(HttpStatus.BAD_REQUEST, "중복된 아이디입니다."),
    IDENTIFYNUM_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "주민번호 형식이 맞지 않습니다."),
    SCHOOL_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "학교 형식이 잘못되었습니다."),
    PERMISSION_ERROR(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    FACILITY_NOT_FOUND(HttpStatus.NOT_FOUND, "시설이 없습니다."),
    HASHTAG_NOT_FOUND(HttpStatus.NOT_FOUND, "해시태그가 존재하지 않습니다."),
    UNIVERSITY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 대학교입니다."),
    BUILDING_NOT_FOUND(HttpStatus.NOT_FOUND, "건물이 존재하지 않습니다."),
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "공지사항이 존재하지 않습니다."),
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "테마가 존재하지 않습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약이 존재하지 않습니다."),
    IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "이미지가 존재하지 않습니다."),
    DUPLICATED_DEPARTMENT(HttpStatus.CONFLICT, "이미 전공(학과)가 존재합니다."),
    DEPARTMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "전공(학과)가 존재하지 않습니다."),
    HEAD_COUNT_INVALID_RANGE(HttpStatus.BAD_REQUEST, "잘못된 예약 인원입니다."),
    MEMBER_EXIST(HttpStatus.BAD_REQUEST, "학생이 존재합니다."),

    REFRESHTOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 RefreshToken이 존재하지 않습니다."),
    SEARCH_CONDITION_ERROR(HttpStatus.BAD_REQUEST, "잘못된 검색어입니다."),
    STAR_RATING_RANGE(HttpStatus.BAD_REQUEST, "별점은 1점 이상 5점 이하의 정수입니다."),
    FACILITY_DAILY_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "이미 예약이 존재합니다."),
    EXTENDED_TIME_ERROR(HttpStatus.BAD_REQUEST, "연장할 시간은 기존 시간보다 이후여야 합니다."),
    RETURN_PHOTO_REQUIRED(HttpStatus.BAD_REQUEST, "반납 사진이 필요합니다."),
    EMAIL_COOL_TIME(HttpStatus.TOO_MANY_REQUESTS, "중복된 인증 메일 전송 요청입니다."),
    WRONG_EMAIL_CODE(HttpStatus.BAD_REQUEST, "잘못된 인증번호입니다."),
    EXPIRED_EMAIL_CODE(HttpStatus.BAD_REQUEST, "만료된 인증번호입니다."),
    WRONG_VERIFICATION_TOKEN(HttpStatus.UNAUTHORIZED, "회원가입할 수 없습니다."),
    MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하는 회원입니다."),
    LIVE_SEAT_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "실시간 현황을 제공하지 않는 학교입니다."),
    DUPLICATED_FACILITY_THEME(HttpStatus.CONFLICT, "테마에 이미 시설이 존재합니다."),
    FACILITY_THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "테마에 시설이 존재하지 않습니다."),
    // 어드민 관련 에러
    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "관리자가 존재하지 않습니다."),
    INVALID_ROLE_VALUE(HttpStatus.FORBIDDEN, "잘못된 역할입니다."),
    ADMIN_NOT_APPROVED(HttpStatus.FORBIDDEN, "승인되지 않은 관리자 계정입니다."),
    ADMIN_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
