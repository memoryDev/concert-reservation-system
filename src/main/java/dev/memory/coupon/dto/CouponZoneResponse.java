package dev.memory.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponZoneResponse {
    private Long couponId; // 쿠폰 고유 ID
    private String couponCode; // 쿠폰 번호
    private String couponName; // 쿠폰 이름
    private Integer discountAmount; // 할인 금액
    private Integer totalQuantity; // 발급된 갯수
    private Integer issuedQuantity; // 총 발급 갯수
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime issueStartDate; // 발급 시작기간

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime issueEndDate; // 발급 종료기간

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime issuedAt; // 발급일

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime expireAt; // 만료일
}
