package dev.memory.coupon.repository;

import dev.memory.coupon.domain.MemberCoupon;

import java.util.Optional;

public interface MemberCouponRepositoryCustom {
    MemberCoupon myCoupons(Long memberId, Long couponId);
    Boolean existsMemberCoupon(Long memberId, Long couponId);
}
