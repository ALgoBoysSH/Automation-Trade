package com.example.autotrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AutomationTradeApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutomationTradeApplication.class, args);
    }
}
