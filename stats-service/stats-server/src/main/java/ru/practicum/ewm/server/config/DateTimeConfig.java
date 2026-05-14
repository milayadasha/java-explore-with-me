package ru.practicum.ewm.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class DateTimeConfig {

    @Value("${ewm.date-time.pattern}")
    private String pattern;

    @Bean
    public String dateTimePattern() {
        return pattern;
    }

    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern(pattern);
    }
}