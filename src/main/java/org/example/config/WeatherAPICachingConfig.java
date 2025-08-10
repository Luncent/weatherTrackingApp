package org.example.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.example.dto.LocationWeatherDTO;
import org.example.model.Coordinate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Configuration
public class WeatherAPICachingConfig {

    //can be configured via properties
    private static final Integer CACHE_TTL = 3;

    @Bean
    public Cache<Coordinate, LocationWeatherDTO> locationsCache(){
        return Caffeine.newBuilder()
                .expireAfterWrite(CACHE_TTL, TimeUnit.MINUTES)
                .build();
    }

}
