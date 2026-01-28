package dev.memory.concert.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertScheduleCreateRequest {

    @NotNull(message = "공연 날짜를 입력해주세요.")
    @FutureOrPresent(message = "공연 날짜는 오늘 이후여야 합니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate concertDate; // 공연 날짜(yyyy-MM-dd)

    @NotNull(message = "공연 시작 시간을 입력해주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime startTime; // 공연 시작 시간(HH:mm)

    @NotNull(message = "공연 종료 시간을 입력해주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime endTime; // 공연 종료 시간(HH:mm)

    @NotNull(message = "예매 시작일을 입력해주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime scheduleStart; // 예매 시작일(yyyy-MM-dd HH:mm)

    @NotNull(message = "예매 종료일을 입력해주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime scheduleEnd; // 예매 종료일(yyyy-MM-dd HH:mm)

    @NotNull(message = "총 좌석수를 입력해주세요.")
    @Positive(message = "총 좌석수는 1개 이상이어야 합니다.")
    private Integer totalSeats; // 총 좌석수

    @NotNull(message = "최소 구매 수량을 입력해주세요.")
    @Min(value = 1, message = "최소 1개 이상 구매 가능해야 합니다.")
    private Integer minPurchaseCount; // 회당 최소 선택 단위

    @NotNull(message = "최대 구매 수량을 입력해주세요.")
    @Min(value = 1, message = "최대 구매 수량은 1개 이상이어야 합니다.")
    private Integer maxPurchaseCount; // 회당 최대 선택 단위

    // 커스텀 검증 로직
    @AssertTrue(message = "공연 시작 시간은 종료 시간보다 빨라야 합니다.")
    public boolean isTimeValid() {
        if (startTime == null || endTime == null) return true;
        return startTime.isBefore(endTime);
    }

    @AssertTrue(message = "예매 시작일은 종료일보다 빨라야 합니다.")
    public boolean isScheduleValid() {
        if (scheduleStart == null || scheduleEnd == null) return true;
        return scheduleStart.isBefore(scheduleEnd);
    }

    @AssertTrue(message = "최대 구매 수량은 최소 구매 수량보다 크거나 같아야 합니다.")
    public boolean isPurchaseCountValid() {
        if (minPurchaseCount == null || maxPurchaseCount == null) return true;
        return maxPurchaseCount >= minPurchaseCount;
    }
}
