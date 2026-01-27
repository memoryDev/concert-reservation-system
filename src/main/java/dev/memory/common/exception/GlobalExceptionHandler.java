package dev.memory.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        // 1. 에러 결과에서 첫 번째 FieldError를 추출
        FieldError fieldError = e.getBindingResult().getFieldError();

        // 2. 에러 메시지 조합 (예: "[email] : 이메일 형식이 올바르지 않습니다")
        String errorMessage = fieldError != null
                ? String.format("[%s] : %s", fieldError.getField(), fieldError.getDefaultMessage())
                : ErrorCode.INVALID_INPUT.getMessage();

        log.error("Validation Error for Request: {}", errorMessage);

        // 3. 커스텀 메시지를 담아 응답 전송
        return ErrorResponse.toResponseEntity(ErrorCode.INVALID_INPUT);
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

    // 5. 그 외 예상치 못한 모든 최상위 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllException(Exception e) {
        log.error("Unhandled Exception: ", e);
        return ErrorResponse.toResponseEntity(ErrorCode.SYSTEM_ERROR);
    }

}
