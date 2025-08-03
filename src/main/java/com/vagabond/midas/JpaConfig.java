package com.vagabond.midas;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.vagabond.midas")
@EntityScan(basePackages = "com.vagabond.midas")
public class JpaConfig {
}
