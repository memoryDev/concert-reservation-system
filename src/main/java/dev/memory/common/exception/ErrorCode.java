package dev.memory.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // --- 서버 시스템 에러 (COMMON) ---
    SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "일시적인 오류가 발생했습니다. 잠시 후 시도해주세요."),

    // --- 공통 에러 (COMMON) ---
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "지원하지 않는 호출 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "서버 내부 오류가 발생했습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C004", "데이터베이스 오류가 발생했습니다."),
    QUERY_PATH_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C005", "조회 경로가 올바르지 않습니다."),

    // --- 인증/인가 (AUTH) ---
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "A001", "인증 정보가 유효하지 않습니다."),
    SESSION_EXPIRED(HttpStatus.UNAUTHORIZED, "A002", "세션이 만료되었습니다. 다시 로그인해주세요."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "A003", "접근 권한이 없습니다."),
    EMPTY_TOKEN(HttpStatus.UNAUTHORIZED, "A004", "로그인이 필요한 서비스입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "A005", "아이디 또는 비밀번호가 일치하지 않습니다."),

    // --- 멤버 관련 (MEMBER) ---
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "존재하지 않는 회원입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "M002", "이미 사용 중인 이메일입니다."),
    DUPLICATE_USER_ID(HttpStatus.CONFLICT, "M003", "이미 사용 중인 아이디입니다."),

    // --- 쿠폰 관련 (COUPON) ---
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "CP01", "해당 쿠폰을 찾을 수 없습니다."),
    COUPON_SOLD_OUT(HttpStatus.BAD_REQUEST, "CP02", "쿠폰이 모두 소진되었습니다."),
    ALREADY_ISSUED(HttpStatus.CONFLICT, "CP03", "이미 발급받은 쿠폰입니다."),
    INVALID_COUPON_DATE(HttpStatus.BAD_REQUEST, "CP04", "쿠폰 사용 기간이 아닙니다."),
    COUPON_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "CP05", "해당 쿠폰은 현재 사용할 수 없습니다."),
    COUPON_NOT_STARTED(HttpStatus.BAD_REQUEST, "CP06", "쿠폰 발급 시작 전입니다."),
    COUPON_EXPIRED(HttpStatus.BAD_REQUEST, "CP07", "쿠폰 발급 기간이 종료되었습니다."),

    // --- 스케줄 및 예약 (SCHEDULE) ---
    SCHEDULE_NOT_ACTIVE(HttpStatus.BAD_REQUEST, "SCH01", "현재 예매 가능한 스케줄이 아닙니다."),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCH02", "존재하지 않는 스케줄입니다."),
    SEAT_ALREADY_BOOKED(HttpStatus.CONFLICT, "SCH03", "이미 예약된 좌석입니다."),
    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "SCH04", "존재하지 않는 좌석입니다."),

    // --- 대기열 관련 (QUEUE) --- ⬅️ 새로 추가
    QUEUE_ENTRY_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Q001", "대기열 진입에 실패했습니다."),
    NOT_IN_QUEUE(HttpStatus.BAD_REQUEST, "Q002", "대기열에 존재하지 않습니다."),
    NOT_ACTIVE_USER(HttpStatus.FORBIDDEN, "Q003", "예매 권한이 없습니다. 대기 순서를 기다려주세요."),

    // --- 시스템/동시성 관련 (S) ---
    SERVER_BUSY(HttpStatus.SERVICE_UNAVAILABLE, "S002", "접속자가 많아 처리가 지연되고 있습니다. 잠시 후 다시 시도해주세요.");


    private final HttpStatus status;
    private final String code;
    private final String message;

}
