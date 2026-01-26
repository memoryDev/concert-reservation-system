package dev.memory.coupon.repository;

import com.querydsl.core.QueryFactory;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.memory.coupon.domain.QCoupon;
import dev.memory.coupon.dto.CouponResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CouponRepositoryCustomImpl implements CouponRepositoryCustom{

    private final JPAQueryFactory queryFactory;


    @Override
    public List<CouponResponse> getPoupons() {
        return queryFactory.select(Projections.constructor(CouponResponse.class,
                QCoupon.coupon.id,
                QCoupon.coupon.code,
                QCoupon.coupon.name,
                QCoupon.coupon.totalQuantity,
                QCoupon.coupon.issuedQuantity,
                QCoupon.coupon.discountAmount,
                QCoupon.coupon.validDays,
                QCoupon.coupon.isActive
                ))
                .from(QCoupon.coupon)
                .orderBy(QCoupon.coupon.id.desc())
                .fetch();
    }
}
