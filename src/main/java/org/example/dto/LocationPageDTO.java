package org.example.dto;

import java.util.List;

public record LocationPageDTO (
        List<LocationWeatherDTO> locationWeatherDTOList,
        int pageNumber,
        Long lastPageNumber
){}
