package org.example.exception_handling.exceptions.weather_api;

public class WeatherApiException extends RuntimeException {
    public WeatherApiException(Throwable cause) {
        super(cause);
    }
}
