package dev.memory.coupon.facade;

import dev.memory.common.exception.CustomException;
import dev.memory.common.exception.ErrorCode;
import dev.memory.coupon.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponIssueFacade {

    private final RedissonClient redissonClient;
    private final UserCouponService userCouponService;

    public void issue(Long memberId, Long couponId) {

        // 1. 쿠폰별로 고유한 락 키 생성
        RLock lock = redissonClient.getLock("coupon_lock:" + couponId);

        try {
            // 2. 락 획득 시도 ( 최대 10초 대기, 1초 동안 점유)
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!available) {
                log.info("락 획득 실패 - couponId: {}", couponId);
                throw new CustomException(ErrorCode.SERVER_BUSY);
            }

            // 3. 실제 비즈니스 로직 실행
            userCouponService.issueCoupon(memberId, couponId);

        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            // 4. 반드시 락 해제
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
