package dev.memory.coupon.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.memory.coupon.domain.MemberCoupon;
import dev.memory.coupon.domain.QCoupon;
import dev.memory.coupon.domain.QMemberCoupon;
import dev.memory.member.domain.QMember;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class MemberCouponRepositoryCustomImpl implements MemberCouponRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public MemberCoupon myCoupons(Long memberId, Long couponId) {

        return queryFactory
                .selectFrom(QMemberCoupon.memberCoupon)
                .where(QCoupon.coupon.id.eq(couponId)
                        .and(QMember.member.id.eq(memberId))
                ).fetchOne();
    }

    @Override
    public Boolean existsMemberCoupon(Long memberId, Long couponId) {

        Integer fetchOne = queryFactory
                .selectOne()
                .from(QMemberCoupon.memberCoupon)
                .where(QMemberCoupon.memberCoupon.member.id.eq(memberId)
                        .and(QMemberCoupon.memberCoupon.coupon.id.eq(couponId))
                )
                .fetchOne();

        return fetchOne != null;
    }


}
