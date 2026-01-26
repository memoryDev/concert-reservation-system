package dev.memory.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {
    private Long id;
    private String code;
    private String name;
    private Integer totalQuantity;
    private Integer issuedQuantity;
    private Integer discountAmount;
    private Integer validDays;
    private Boolean isActive;
}
