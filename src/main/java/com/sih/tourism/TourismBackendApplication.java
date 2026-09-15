package com.sih.tourism;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@org.springframework.scheduling.annotation.EnableScheduling
public class TourismBackendApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TourismBackendApplication.class, args);
    }

}
