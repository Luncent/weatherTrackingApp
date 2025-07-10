package org.example.controllers;

import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public TestController testController() {
        return new TestController();
    }
}
