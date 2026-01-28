package dev.memory.coupon.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponStatusUpdateRequest {

    @NotNull(message = "쿠폰 활성여부를 선택해주세요.")
    private Boolean isActive; // 활성 여부
}
