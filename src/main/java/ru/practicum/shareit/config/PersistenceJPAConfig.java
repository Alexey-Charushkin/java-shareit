package ru.practicum.shareit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "ru.practicum.shareit")
class PersistenceJPAConfig {

}
