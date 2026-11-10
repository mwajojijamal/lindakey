package com.lindakey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class LindakeyApplication {
    public static void main(String[] args) {
        SpringApplication.run(LindakeyApplication.class, args);
    }
}