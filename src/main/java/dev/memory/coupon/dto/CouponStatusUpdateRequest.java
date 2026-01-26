package dev.memory.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponStatusUpdateRequest {
    private Boolean isActive; // 활성 여부
}
