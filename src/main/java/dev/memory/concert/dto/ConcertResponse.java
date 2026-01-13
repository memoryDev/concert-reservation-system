package dev.memory.concert.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertResponse {
    private Long id;
    private String name;
    private Integer price;
    private LocalDate concertStartDate;
    private LocalDate concertEndDate;
}
