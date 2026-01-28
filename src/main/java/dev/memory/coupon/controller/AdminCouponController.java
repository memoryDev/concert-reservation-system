package dev.memory.coupon.controller;

import dev.memory.common.exception.CustomException;
import dev.memory.common.exception.ErrorCode;
import dev.memory.coupon.dto.CouponCreateRequest;
import dev.memory.coupon.dto.CouponResponse;
import dev.memory.coupon.dto.CouponStatusUpdateRequest;
import dev.memory.coupon.service.AdminCouponService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Void> createCoupon(@Valid @RequestBody CouponCreateRequest request) {

        // validateRequest(request);

        adminCouponService.createCoupon(request);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateCouponStatus(@PathVariable Long id, @Valid @RequestBody CouponStatusUpdateRequest request) {

        if (id == null || id == 0) {
            throw new CustomException(ErrorCode.COUPON_NOT_FOUND);
        }

        adminCouponService.updateCouponStatus(id, request);

        return ResponseEntity.ok().build();

    }

}
