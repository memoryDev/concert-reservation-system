package dev.memory.concert.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@ToString
public class ConcertScheduleCreateRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate concertDate; // 공연 날짜(yyyy-MM-dd)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime startTime; // 공연 시작 시간(HH:mm)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime endTime; // 공연 종료 시간(HH:mm)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime scheduleStart; // 예매 시작일(yyyy-MM-dd HH:mm)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime scheduleEnd; // 예매 종료일(yyyy-MM-dd HH:mm)
    private Integer totalSeats; // 총 좌석수
    private Integer minPurchaseCount; // 회당 최소 선택 단위
    private Integer maxPurchaseCount; // 회당 최대 선택 단위
}
