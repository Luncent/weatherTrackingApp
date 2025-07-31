package org.example.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.dto.LocationPageDTO;
import org.example.dto.LocationWeatherDTO;
import org.example.entities.Location;
import org.example.entities.User;
import org.example.exceptions.EntityExistsException;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.UnauthorizedException;
import org.example.repositories.repos_impl.LocationRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class LocationService {
    private final LocationRepository locationRepository;
    private final UserService userService;
    private final WeatherAPIService weatherAPIService;

    /**
     @throws EntityNotFoundException if user not found
     @throws EntityExistsException if location already added by user
     */
    @Transactional
    public Location save(String cityName, BigDecimal latitude, BigDecimal longitude, Long userId) throws EntityNotFoundException, EntityExistsException {
        User user = userService.findById(userId);
        Location newLocation = Location.builder()
                .name(cityName)
                .latitude(latitude)
                .longitude(longitude)
                .build();
        newLocation.setUser(user);
        try {
            locationRepository.save(newLocation);
        }
        catch (ConstraintViolationException e) {
            log.debug("Error saving location", e);
            throw new EntityExistsException("User already added location with such name and coordinates");
        }
        return newLocation;
    }

    public Location findById(Long id) throws EntityNotFoundException {
        Optional<Location> locationOptional = locationRepository.getById(id);
        if (locationOptional.isEmpty()) {
            log.debug("could not found location with such id {}", id);
            throw new EntityNotFoundException();
        }
        return locationOptional.get();
    }


    @Transactional
    public void delete(Long locationId, Long userId) throws EntityNotFoundException, UnauthorizedException {
        Optional<Location> locationOptional = locationRepository.getById(locationId);
        if (locationOptional.isEmpty()) {
            log.debug("could not found location with such id {}", locationId);
            throw new EntityNotFoundException();
        }
        Location location = locationOptional.get();
        Long locationUserId = location.getUser().getId();
        if(!locationUserId.equals(userId)){
            log.debug("User cant delete others locations. UserId current {}, location userId{}", userId, locationUserId);
            throw new UnauthorizedException();
        }
        log.debug("user with id ({}) deleting location with user id ({})", userId, locationUserId);
        locationRepository.delete(locationId);
    }

    @Transactional
    public LocationPageDTO selectPaginated(int pageNumber) throws Exception {
        List<Location> locations = locationRepository.getPage(pageNumber);
        List<LocationWeatherDTO> locationWeatherDTOS = new ArrayList<>();
        for (Location location : locations) {
            locationWeatherDTOS.add(weatherAPIService.getLocationWeatherByCoordinates(location.getLongitude(), location.getLatitude()));
        }
        Long lastPageNumber = locationRepository.getPageCount();
        return new LocationPageDTO(locationWeatherDTOS, pageNumber, lastPageNumber);
    }

}
