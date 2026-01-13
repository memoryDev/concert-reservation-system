package dev.memory.concert.repository;

import dev.memory.concert.dto.ConcertResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomConcertRepository {

    Page<ConcertResponse> getContents(Pageable pageable);
}
