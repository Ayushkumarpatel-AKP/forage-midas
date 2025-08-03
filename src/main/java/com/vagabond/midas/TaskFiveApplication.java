package com.vagabond.midas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.jpmc.midascore.entity")
@EnableJpaRepositories("com.jpmc.midascore.repository")
public class TaskFiveApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskFiveApplication.class, args);
    }
}
