package dev.memory.queue.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest {

    @NotNull(message = "콘서트 정보가 없습니다.")
    private Long concertId;
}
