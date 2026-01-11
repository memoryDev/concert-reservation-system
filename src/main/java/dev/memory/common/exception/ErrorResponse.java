package dev.memory.common.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {

    private int status;
    private String error;
    private String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(int status, String error, String message) {
        return ResponseEntity.status(status).body(ErrorResponse.builder().status(status).error(error).message(message).build());
    }

}
