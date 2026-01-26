package dev.memory.coupon.controller;

import dev.memory.coupon.dto.CouponCreateRequest;
import dev.memory.coupon.dto.CouponResponse;
import dev.memory.coupon.dto.CouponStatusUpdateRequest;
import dev.memory.coupon.service.AdminCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/coupons")
public class AdminCouponController {

    private final AdminCouponService adminCouponService;

    @GetMapping
    public ResponseEntity<Object> getCoupons() {

        List<CouponResponse> coupons = adminCouponService.getCoupons();

        return ResponseEntity.ok().body(coupons);
    }

    @PostMapping
    public ResponseEntity<Void> createCoupon(@RequestBody CouponCreateRequest request) {

        // TODO 유효성 검사
        validateRequest(request);

        adminCouponService.createCoupon(request);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateCouponStatus(@PathVariable Long id, @RequestBody CouponStatusUpdateRequest request) {

        if (id == null || id == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }

        if (request.getIsActive() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }

        adminCouponService.updateCouponStatus(id, request);

        return ResponseEntity.ok().build();

    }

    private void validateRequest(CouponCreateRequest req) {
        // 1. 필수값(Null) 및 빈 문자열 체크
        if (!StringUtils.hasText(req.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "쿠폰 이름은 필수입니다.");
        }
        if (req.getDiscountAmount() == null || req.getDiscountAmount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "할인 금액은 0보다 커야 합니다.");
        }
        if (req.getTotalQuantity() == null || req.getTotalQuantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "발행 수량은 1개 이상이어야 합니다.");
        }
        if (req.getValidDays() == null || req.getValidDays() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효 기간은 1일 이상이어야 합니다.");
        }
        if (req.getIsActive() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "활성 여부를 입력해주세요.");
        }

        // 2. 날짜 체크
        if (req.getIssueStartDate() == null || req.getIssueEndDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "발급 기간(시작일, 종료일)은 필수입니다.");
        }

        // 3. 날짜 논리 체크 (종료일이 시작일보다 빨라서는 안 됨)
        if (req.getIssueEndDate().isBefore(req.getIssueStartDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "발급 종료일은 시작일보다 빠를 수 없습니다.");
        }
    }


}
