package org.example.dto.locations;

import java.util.List;

public record LocationPageDTO (
        List<LocationWeatherDTO> locationWeatherDTOList,
        Integer currentPage,
        Long overallPages
){}
