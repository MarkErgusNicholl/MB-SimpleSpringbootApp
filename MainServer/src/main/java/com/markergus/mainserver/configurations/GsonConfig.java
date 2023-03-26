package com.markergus.mainserver.configurations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GsonConfig {

    @Bean
    public Gson gson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson;
    }
}
