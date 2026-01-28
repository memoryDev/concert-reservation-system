package dev.memory.coupon.service;

import dev.memory.coupon.domain.Coupon;
import dev.memory.coupon.dto.CouponCreateRequest;
import dev.memory.coupon.dto.CouponResponse;
import dev.memory.coupon.dto.CouponStatusUpdateRequest;
import dev.memory.coupon.repository.CouponRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCouponService {

    private final CouponRepository couponRepository;

    public List<CouponResponse> getCoupons() {
        return couponRepository.getPoupons();
    }

    @Transactional
    public void createCoupon(CouponCreateRequest request) {

        // 엔티티 생성
        Coupon coupon = Coupon.createCoupon(request.getName(),
                request.getDiscountAmount(),
                request.getTotalQuantity(),
                request.getValidDays(),
                request.getIssueStartDate(),
                request.getIssueEndDate(),
                request.getIsActive());

        couponRepository.save(coupon);
    }

    @Transactional
    public void updateCouponStatus(Long id, @Valid @RequestBody CouponStatusUpdateRequest request) {

        // 1. 쿠폰 조회
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 쿠폰을 찾을수 없습니다."));

        // 2. 상태 변경
        coupon.updateCoupon(request.getIsActive());
    }

}
