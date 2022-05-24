package com.car.instant.messenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class InstantMessengerApplication {
    public static void main(String[] args) {
        SpringApplication.run(InstantMessengerApplication.class, args);
    }

}

