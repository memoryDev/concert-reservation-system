package dev.memory.coupon.repository;

import com.querydsl.core.QueryFactory;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.memory.coupon.domain.QCoupon;
import dev.memory.coupon.domain.QMemberCoupon;
import dev.memory.coupon.dto.CouponResponse;
import dev.memory.coupon.dto.CouponZoneResponse;
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

    @Override
    public List<CouponZoneResponse> getIssuableCoupons(Long memberId) {

        return queryFactory
                .select(Projections.constructor(CouponZoneResponse.class,
                        QCoupon.coupon.id,
                        QCoupon.coupon.code,
                        QCoupon.coupon.name,
                        QCoupon.coupon.discountAmount,
                        QCoupon.coupon.totalQuantity,
                        QCoupon.coupon.issuedQuantity,
                        QCoupon.coupon.issueStartDate,
                        QCoupon.coupon.issueEndDate,
                        QMemberCoupon.memberCoupon.issuedAt,
                        QMemberCoupon.memberCoupon.expireAt
                ))
                .from(QCoupon.coupon)
                .leftJoin(QMemberCoupon.memberCoupon)
                .on(
                        QCoupon.coupon.eq(QMemberCoupon.memberCoupon.coupon),
                        memberIdEq(memberId)
                )
                .where(QCoupon.coupon.isActive.eq(true)
                )
                .fetch();
    }

private BooleanExpression memberIdEq(Long memberId) {
    if (memberId != null) {
        // 로그인: 내 아이디랑 같은지 확인
        return QMemberCoupon.memberCoupon.member.id.eq(memberId);
    } else {
        // 비로그인: 무조건 조인 실패 처리 (1 != 1 같은 거짓 조건 생성)
        // 이렇게 해야 Left Join 결과가 null이 되어 "미수령" 상태가 됨
        return Expressions.asBoolean(true).isFalse();
    }
}
}
