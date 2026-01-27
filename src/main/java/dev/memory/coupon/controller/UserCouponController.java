package dev.memory.coupon.controller;

import dev.memory.coupon.dto.CouponZoneResponse;
import dev.memory.coupon.facade.CouponIssueFacade;
import dev.memory.coupon.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
@Slf4j
public class UserCouponController {

    private final UserCouponService userCouponService;
    private final CouponIssueFacade couponIssueFacade;

    @GetMapping("/issuable")
    public ResponseEntity<List<CouponZoneResponse>> getIssuableCoupons(
            @AuthenticationPrincipal User user
    ){

        Long memberId = (user != null) ? Long.parseLong(user.getUsername()) : null;

        List<CouponZoneResponse> issuableCoupons = userCouponService.getIssuableCoupons(memberId);

        return ResponseEntity.ok().body(issuableCoupons);
    }

    @PostMapping("/{couponId}/issue")
    public ResponseEntity<Void> issueCoupon(@AuthenticationPrincipal User user, @PathVariable("couponId") Long couponId) throws BadRequestException {

        if (couponId == null || couponId == 0) {
            throw new BadRequestException("쿠폰이 존재하지 않습니다.");
        }

        couponIssueFacade.issue(Long.parseLong(user.getUsername()), couponId);

        return ResponseEntity.ok().build();

    }


}
