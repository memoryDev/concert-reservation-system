package dev.memory.concertschedule.domain;

public enum BookingStatus {
    PENDING,    // 예매 대기 (예매 시작 전)
    ACTIVE,     // 예매 활성 (예매 가능)
    CLOSED      // 예매 종료 (예매 마감)
}