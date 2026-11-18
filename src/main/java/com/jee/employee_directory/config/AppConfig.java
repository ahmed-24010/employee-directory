package com.jee.employee_directory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper; // Jackson 3 : tools.jackson.*

@Configuration
public class AppConfig {

    @Bean
    public JsonMapper jsonMapper() {
        return new JsonMapper();
    }
}
