package org.example.services;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.dto.LocationPageDTO;
import org.example.dto.LocationWeatherDTO;
import org.example.entities.Location;
import org.example.entities.User;
import org.example.exceptions.EntityExistsException;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.UnauthorizedException;
import org.example.mappers.LocationMapper;
import org.example.model.Coordinate;
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
    private final Cache<Coordinate, LocationWeatherDTO> locationWeatherCache;

    /**
     * @throws EntityNotFoundException if user not found
     * @throws EntityExistsException   if location already added by user
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
        } catch (ConstraintViolationException e) {
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
    public void delete(BigDecimal latitude, BigDecimal longitude, Long userId) throws EntityNotFoundException{
        Optional<Location> locationOptional = locationRepository.getByCoordinatesAndUserId(latitude, longitude, userId);
        if (locationOptional.isEmpty()) {
            log.debug("could not found location with such coordinates lat:{}, lon:{}", latitude, longitude);
            throw new EntityNotFoundException();
        }
        Location location = locationOptional.get();
        //not needed
        /*Long locationUserId = location.getUser().getId();
        if(!locationUserId.equals(userId)){
            log.debug("User cant delete others locations. UserId current {}, location userId{}", userId, locationUserId);
            throw new UnauthorizedException();
        }*/
        log.debug("user with id ({}) deleting location with name ({})", userId, location.getName());
        locationRepository.delete(location.getId());
    }

    @Transactional(readOnly = true)
    public LocationPageDTO selectPaginated(int pageNumber, Long userID) throws Exception {
        List<Location> locations = locationRepository.getPage(pageNumber, userID);
        List<LocationWeatherDTO> locationWeatherDTOS = new ArrayList<>();
        for (Location location : locations) {

            Coordinate coordinate = new Coordinate(location.getLatitude(), location.getLongitude());
            LocationWeatherDTO dto = null;
            if((dto = locationWeatherCache.getIfPresent(coordinate))==null){
                log.debug("could not find location with such coordinate in cache {}", coordinate);
                dto = weatherAPIService.getLocationWeatherByCoordinates(coordinate);
                locationWeatherCache.put(coordinate, dto);
            }
            else{
                log.debug(" location with coordinates {} found in cache", coordinate);
            }
            locationWeatherDTOS.add(dto);
        }
        Long lastPageNumber = locationRepository.getPageCount(userID);
        return new LocationPageDTO(locationWeatherDTOS, pageNumber, lastPageNumber);
    }

}
