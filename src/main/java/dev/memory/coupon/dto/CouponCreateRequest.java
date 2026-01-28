package dev.memory.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CouponCreateRequest {

    @NotBlank(message = "쿠폰 이름을 입력해 주세요.")
    private String name;                    // 이름

    @NotNull(message = "할인금액 입력해 주세요.")
    @Min(value = 10, message = "할인 금액은 최소 10원부터 설정 가능합니다.")
    private Integer discountAmount;         // 할인금액

    @NotNull(message = "발행할 쿠폰의 총 개수를 입력해 주세요.")
    @Min(value = 1, message = "최소 1개 이상의 쿠폰을 발행해야 합니다.")
    private Integer totalQuantity;          // 총 발행 개수

    @NotNull(message = "쿠폰 유효 기간을 입력해 주세요.")
    @Min(value = 1, message = "유효 기간은 최소 1일 이상이어야 합니다.")
    private Integer validDays;              // 유효기간(일단위)

    @NotNull(message = "쿠폰 발급 시작 기간을 선택해 주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime issueStartDate;   // 발급 시작기간

    @NotNull(message = "쿠폰 발급 종료 시점을 선택해 주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime issueEndDate;     // 발급 종료기간

    @NotNull(message = "쿠폰 활성여부를 선택해주세요.")
    private Boolean isActive;               // 활성여부(true/false)

    @AssertTrue(message = "할인 금액은 10원 단위로만 설정할 수 있습니다.")
    public boolean isDiscountAmountValid() {
        if (discountAmount == null) return true;
        return discountAmount % 10 == 0;
    }

    @AssertTrue(message = "발급 종료 시점은 시작 시점 이후여야 합니다.")
    public boolean isScheduleValid() {
        if (issueStartDate == null || issueEndDate == null) return true;
        return issueStartDate.isBefore(issueEndDate);
    }
}
