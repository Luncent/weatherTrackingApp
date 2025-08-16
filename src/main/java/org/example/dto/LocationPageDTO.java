package org.example.dto;

import java.util.List;

public record LocationPageDTO (
        List<LocationWeatherDTO> locationWeatherDTOList,
        Integer currentPage,
        Long overallPages
){}
