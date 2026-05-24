package com.chaosopen.ddd.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.chaosopen.ddd")
public class DddTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(DddTemplateApplication.class, args);
    }
}
