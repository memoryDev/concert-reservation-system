package dev.memory.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String name;                    // 이름
    private Integer discountAmount;         // 할인금액
    private Integer totalQuantity;          // 총 발행 개수
    private Integer validDays;              // 유효기간(일단위)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime issueStartDate;   // 발급 시작기간

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime issueEndDate;     // 발급 종료기간
    private Boolean isActive;               // 활성여부(true/false)
}
