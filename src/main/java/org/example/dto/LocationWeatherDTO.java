package org.example.dto;

import lombok.*;

import java.math.BigDecimal;


public record LocationWeatherDTO(BigDecimal temperature, BigDecimal feelsLikeTemperature, Integer humidity,
                                 BigDecimal longitude, BigDecimal latitude, String countryCode,
                                 String weatherDescription, String city, String weatherIcon) {
}
/*
<div class="position-absolute weather-card-delete-form">
                                <button class="btn-close" aria-label="Delete"></button>
                            </div>
                            <img class="card-img-top img-fluid"
th:src="@{'/static/images/'+${location.weatherIcon()}+'.png'}" alt="Weather icon">
                            <div class="card-body d-flex flex-column">
                                <h1 class="card-text" th:text="${location.temperature()}+'°C'">7°C</h1>
                                <h3 class="card-title" th:text="${location.city()} + ', ' + ${location.countryCode()} ">
Tbilisi, GE</h3>
                                <h2 class="card-title"
th:text="'lat: '+${location.latitude()} + ', lon: ' + ${location.longitude()} "></h2>
                                <p class="card-text mb-1">Feels like <span
th:text="${location.feelsLikeTemperature()}">2</span>°C.
                                    <span th:text="${location.weatherDescription()}">Clear sky</span>
                                </p>
                                <p class="card-text mb-1" th:text="'Humidity: '${location.humidity()}+'%'">Humidity:
        70%</p>
                            </div>*/

