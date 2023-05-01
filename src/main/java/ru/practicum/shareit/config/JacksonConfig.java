package ru.practicum.shareit.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.databind.Module;

@Configuration
public class JacksonConfig {
    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }

    @Bean
    public Module javaTimeModule() {
        return new JavaTimeModule();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(hibernate5Module());
        mapper.registerModule(javaTimeModule());
        return mapper;
    }
}
