package org.example.config;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class OtherBeansConfig {

    @Bean
    public JsonMapper jsonMapper() {
        return new JsonMapper();
    }

    @Bean
    public HttpClient httpClient(){
        return HttpClient.newBuilder().build();
    }
}
