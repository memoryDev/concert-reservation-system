package dev.memory.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 1. 직접 정의한 커스텀 비즈니스 예외
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("CustomException: {}", e.getMessage());
        // CustomException이ErrorCode를 필드로 가지고 있다면 아래와 같이 사용 가능합니다.
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    // 2. DTO @Valid 유효성 검사 실패 시 발생
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();

        // 에러 메시지 추출 (필드명 포함 여부는 취향껏 선택하세요!)
        String errorMessage = fieldError != null
                ? fieldError.getDefaultMessage() // "비밀번호는 8자 이상이어야 합니다."만 출력
                : ErrorCode.INVALID_INPUT.getMessage();

        log.error("Validation Error: {}", errorMessage);

        return ErrorResponse.toResponseEntity(ErrorCode.INVALID_INPUT, errorMessage);
    }

    // 3. QueryDSL / Hibernate 경로 에러 (시스템 에러 성격)
    @ExceptionHandler(org.hibernate.query.SemanticException.class)
    public ResponseEntity<ErrorResponse> handleSemanticException(org.hibernate.query.SemanticException e) {
        log.error("Query Path Error: {}", e.getMessage());
        return ErrorResponse.toResponseEntity(ErrorCode.QUERY_PATH_ERROR);
    }

    // 4. 데이터베이스 제약 조건 위반 (중복 키 등)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataException(DataIntegrityViolationException e) {
        log.error("Database Violation: {}", e.getMessage());
        return ErrorResponse.toResponseEntity(ErrorCode.DATABASE_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        log.error("Login Failed: {}", e.getMessage());
        return ErrorResponse.toResponseEntity(ErrorCode.LOGIN_FAILED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String errorMessage = ErrorCode.INVALID_INPUT.getMessage();

        if (e.getCause() instanceof InvalidFormatException invalidFormatException) {
            // 에러가 발생한 필드의 타입 확인
            Class<?> targetType = invalidFormatException.getTargetType();

            // 타입에 따라 메세지 분기 처리
            if (targetType.equals(LocalDateTime.class)) {
                errorMessage = "날짜와 시간 형식이 올바르지 않습니다. (2026-01-01 09:00 형식으로 입력해주세요.)";
            } else if (targetType.equals(LocalDate.class)) {
                errorMessage = "날짜 형식이 올바르지 않습니다. (2026-01-01 형식으로 입력해주세요.)";
            } else if (targetType.equals(LocalTime.class)) {
                errorMessage = "시간 형식이 올바르지 않습니다. (09:00 형식으로 입력해주세요.)";
            } else if (targetType.equals(Boolean.class)) {
                errorMessage = "선택 항목을 확인해 주세요. (예/아니오, 또는 True/False)";
            }

            log.debug("Parsing Error: {}", errorMessage);
        }

        return ErrorResponse.toResponseEntity(ErrorCode.INVALID_INPUT, errorMessage);
    }

    // 5. 그 외 예상치 못한 모든 최상위 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllException(Exception e) {
        log.error("Unhandled Exception: ", e);
        return ErrorResponse.toResponseEntity(ErrorCode.SYSTEM_ERROR);
    }

}
