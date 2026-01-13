package dev.memory.concert.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertScheduleResponse {
    private LocalDateTime scheduleStart;
    private LocalDateTime scheduleEnd;
}
