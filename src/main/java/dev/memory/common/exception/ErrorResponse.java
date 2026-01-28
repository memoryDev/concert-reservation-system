package dev.memory.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.builder()
                        .status(errorCode.getStatus().value())
                        .error(errorCode.getStatus().name()) // 예: "UNAUTHORIZED"
                        .code(errorCode.getCode())           // 예: "A001"
                        .message(errorCode.getMessage())     // 예: "세션이 만료되었습니다."
                        .build());
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode, String message) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.builder()
                        .status(errorCode.getStatus().value())
                        .error(errorCode.getStatus().name()) // 예: "UNAUTHORIZED"
                        .code(errorCode.getCode())           // 예: "A001"
                        .message(message)     // 예: "세션이 만료되었습니다."
                        .build());
    }

}
