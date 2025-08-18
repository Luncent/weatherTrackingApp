package org.example.services;

import com.github.benmanes.caffeine.cache.Cache;
import jakarta.persistence.EntityManager;
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
import org.hibernate.SessionFactory;
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
    private final SessionFactory sessionFactory;

    /**
     * @throws EntityNotFoundException if user not found
     * @throws EntityExistsException   if location already added by user
     */
    //TODO dont need to send request for user, use EntityManager.getReferrense();
    @Transactional(rollbackFor = EntityExistsException.class)
    public Location save(String cityName, BigDecimal latitude, BigDecimal longitude,
                         Long userId) throws EntityNotFoundException, EntityExistsException {
        //User user = userService.findById(userId);
        log.debug("Saving location started");
        User user = sessionFactory.getCurrentSession().getReference(User.class, userId);
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
            throw new EntityNotFoundException("could not found location");
        }
        return locationOptional.get();
    }

    //TODO delete by coordinate because of cache
    //TODO cs when looking for locations to view we use coordinates ->
    //TODO if we delete location(id=1) and add same location(new id=2)
    //TODO and try to delete it again we will use id=1 from cache.
    //TODO Clearing cache is not an option as we loose caching for a group of users
    @Transactional
    public void delete(Coordinate coordinate, Long userId) throws EntityNotFoundException{
        Integer rowsDeleted = locationRepository.deleteUserLocation(coordinate.getLatitude(), coordinate.getLongitude(), userId);
        if (rowsDeleted==0) {
            log.debug("User failed to delete location as 0 locations where deleted. Location = {}, userId={}", coordinate, userId);
            throw new EntityNotFoundException("location not found");
        }
        log.debug("user with id ({}) deleted location with ({})", userId, coordinate);
    }

    //TODO mb collect Futures and in the  end get Values
    @Transactional(readOnly = true)
    public LocationPageDTO selectPaginated(int pageNumber, Long userID) throws Exception {
        List<Location> locations = locationRepository.getPage(pageNumber, userID);
        List<LocationWeatherDTO> locationWeatherDTOS = new ArrayList<>();
        Long lastPageNumber = locationRepository.getPageCount(userID);
        for (Location location : locations) {

            Coordinate coordinate = new Coordinate(location.getLatitude(), location.getLongitude());
            LocationWeatherDTO dto = null;
            if((dto = locationWeatherCache.getIfPresent(coordinate))==null){
                log.debug("could not find location with such coordinate in cache {}", coordinate);
                dto = weatherAPIService.getLocationWeatherByCoordinates(coordinate, location.getId());
                dto.setCity(location.getName());
                dto.setLatitude(location.getLatitude());
                dto.setLongitude(location.getLongitude());
                locationWeatherCache.put(coordinate, dto);
            }
            else{
                log.debug(" location with coordinates {} found in cache", coordinate);
            }
            locationWeatherDTOS.add(dto);
        }

        return new LocationPageDTO(locationWeatherDTOS, pageNumber, lastPageNumber);
    }

}
