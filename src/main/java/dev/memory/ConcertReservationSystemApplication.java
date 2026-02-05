package dev.memory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ConcertReservationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcertReservationSystemApplication.class, args);
	}

}
