package dev.memory.concert.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class ConcertCreateRequest {
    private String name;
    private Integer price;
    private List<ConcertScheduleCreateRequest> schedules;
}
