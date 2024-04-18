package org.airway.airwaybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AirwayBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirwayBackendApplication.class, args);
    }

}
