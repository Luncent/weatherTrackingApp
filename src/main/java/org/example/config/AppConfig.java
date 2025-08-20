package org.example.config;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

import java.net.http.HttpClient;

@Configuration
@ComponentScan(basePackages = {"org.example"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class AppConfig {

    @Bean
    public JsonMapper jsonMapper() {
        return new JsonMapper();
    }

    @Bean
    public HttpClient httpClient(){
        return HttpClient.newBuilder().build();
    }

}
