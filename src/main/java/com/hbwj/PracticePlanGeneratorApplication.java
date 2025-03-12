package com.hbwj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
public class PracticePlanGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PracticePlanGeneratorApplication.class, args);
    }
}