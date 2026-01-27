package dev.memory.coupon.repository;

import dev.memory.coupon.dto.CouponResponse;
import dev.memory.coupon.dto.CouponZoneResponse;

import java.util.List;

public interface CouponRepositoryCustom {

    List<CouponResponse> getPoupons();
    List<CouponZoneResponse> getIssuableCoupons(Long memberId);
}
