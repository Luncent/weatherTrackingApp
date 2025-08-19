package org.example.services;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.dto.locations.LocationPageDTO;
import org.example.dto.locations.LocationWeatherDTO;
import org.example.dto.locations.LocationSaveDTO;
import org.example.entities.Location;
import org.example.entities.User;
import org.example.exceptions.EntityExistsException;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.LocationMapper;
import org.example.model.Coordinate;
import org.example.model.LocationData;
import org.example.repositories.repos_impl.LocationRepository;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class LocationService {
    private final LocationRepository locationRepository;
    private final WeatherAPIService weatherAPIService;
    private final Cache<Coordinate, LocationWeatherDTO> locationWeatherCache;
    private final SessionFactory sessionFactory;
    private final LocationMapper locationMapper;

    public Location save(LocationSaveDTO dto, Long userId) throws EntityExistsException {
        log.debug("Saving location started");
        User user = sessionFactory.getCurrentSession().getReference(User.class, userId);
        Location newLocation = locationMapper.map(dto, user);
        try {
            locationRepository.save(newLocation);
        } catch (ConstraintViolationException e) {
            log.debug("Error saving location", e);
            throw new EntityExistsException("User already added location with such name and coordinates");
        }
        return newLocation;
    }

    public Location findById(Long id) throws EntityNotFoundException {
        return locationRepository.getById(id)
                .orElseThrow(()-> new EntityNotFoundException("Location with such id not found"));
    }

    public void delete(Coordinate coordinate, Long userId) throws EntityNotFoundException{
        Integer rowsDeleted = locationRepository.deleteUserLocation(coordinate.getLatitude(), coordinate.getLongitude(), userId);
        if (rowsDeleted==0) {
            log.debug("User failed to delete location as 0 locations where deleted. Location = {}, userId={}", coordinate, userId);
            throw new EntityNotFoundException("location not found");
        }
        log.debug("user with id ({}) deleted location with ({})", userId, coordinate);
    }

    @Transactional(readOnly = true)
    public LocationPageDTO selectPaginated(int pageNumber, Long userID) throws Exception {
        List<Location> locations = locationRepository.getPage(pageNumber, userID);
        Long lastPageNumber = locationRepository.getPageCount(userID);

        List<LocationWeatherDTO> locationWeatherDTOS = new ArrayList<>();
        List<LocationData> cacheAbsentLocations = new ArrayList<>();
        for (Location location : locations) {

            Coordinate coordinate = new Coordinate(location.getLatitude(), location.getLongitude());
            LocationWeatherDTO dto = null;
            if((dto = locationWeatherCache.getIfPresent(coordinate))==null){
                log.debug("could not find location with such coordinate in cache {}", coordinate);
                cacheAbsentLocations.add(new LocationData(coordinate, location.getName(),
                        location.getLatitude(), location.getLongitude()));
            }
            else{
                log.debug(" location with coordinates {} found in cache", coordinate);
                locationWeatherDTOS.add(dto);
            }
        }

        for(LocationWeatherDTO weatherDTO : weatherAPIService.getLocationsWeatherByCoordinates(cacheAbsentLocations)){
            locationWeatherCache.put(new Coordinate(weatherDTO.getLatitude(), weatherDTO.getLongitude()), weatherDTO);
            locationWeatherDTOS.add(weatherDTO);
        }

        return new LocationPageDTO(locationWeatherDTOS, pageNumber, lastPageNumber);
    }

}
