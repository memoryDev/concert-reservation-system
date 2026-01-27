package dev.memory.coupon.service;

import dev.memory.common.enums.DelStatus;
import dev.memory.common.exception.CustomException;
import dev.memory.common.exception.ErrorCode;
import dev.memory.coupon.domain.Coupon;
import dev.memory.coupon.domain.MemberCoupon;
import dev.memory.coupon.dto.CouponZoneResponse;
import dev.memory.coupon.repository.CouponRepository;
import dev.memory.coupon.repository.MemberCouponRepository;
import dev.memory.member.domain.Member;
import dev.memory.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCouponService {

    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    public List<CouponZoneResponse> getIssuableCoupons(Long memberId) {

        return couponRepository.getIssuableCoupons(memberId);
    }

    @Transactional
    public void issueCoupon(Long memberId, Long couponId) {

        // 쿠폰 조회
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

        // 1. 활성여부 체크
        Boolean isActive = coupon.getIsActive();
        if (Boolean.FALSE.equals(isActive)) {
            throw new CustomException(ErrorCode.COUPON_NOT_AVAILABLE);
        }

        // 2. 발급갯수 체크
        Integer totalQuantity = coupon.getTotalQuantity();
        Integer issuedQuantity = coupon.getIssuedQuantity();
        if (totalQuantity <= issuedQuantity) {
            throw new CustomException(ErrorCode.COUPON_SOLD_OUT);
        }

        // 3. 발급 기간인지 체크
        coupon.validateIssuePeriod();

        // 4. 발급조회
        Boolean isAlreadyIssued = memberCouponRepository.existsMemberCoupon(memberId, couponId);
        if (Boolean.TRUE.equals(isAlreadyIssued)) {
            throw new CustomException(ErrorCode.ALREADY_ISSUED);
        }

        // 5. 수량 추가
        coupon.increaseIssuedQuantity();

        // 6. 멤버 조회
        Member member = memberRepository.findByIdAndDelStatus(memberId, DelStatus.N)
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보가 존재하지 않습니다."));

        // 6. 쿠폰 발급 추가
        MemberCoupon memberCoupon = MemberCoupon.createMemberCoupon(member, coupon);

        memberCouponRepository.save(memberCoupon);
    }
}
