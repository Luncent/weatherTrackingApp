package integration;

import annotations.IT;
import org.example.entities.Location;
import org.example.exceptions.EntityExistsException;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.UnauthorizedException;
import org.example.services.LocationService;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static test_constants.LocationDTOConstants.LOCATION_WEATHER_DTO;

@IT
public class LocationServiceTest {

    @Autowired
    LocationService locationService;

    @Autowired
    SessionFactory sessionFactory;

    @Test
    public void saveNewLocationSuccess() throws EntityNotFoundException, EntityExistsException {
        Long userId = 1L;

        Location location = locationService
                .save(LOCATION_WEATHER_DTO.name(), LOCATION_WEATHER_DTO.latitude(), LOCATION_WEATHER_DTO.longitude(), userId);
        Long savedLocationId = location.getId();

        sessionFactory.getCurrentSession().detach(location);

        location = null;

        location = locationService.findById(savedLocationId);

        assertThat(location.getName()).isEqualTo(LOCATION_WEATHER_DTO.name());
    }

    @Test
    public void saveLocationWhenUserAlreadySavedItOrUserNotExists() throws EntityNotFoundException, EntityExistsException {
        Long existingUserId = 1L;
        Long nonExistingUserId = -12L;

        locationService.save(LOCATION_WEATHER_DTO.name(), LOCATION_WEATHER_DTO.latitude(), LOCATION_WEATHER_DTO.longitude(), existingUserId);

        assertAll(
                () -> assertThrows(EntityExistsException.class, () -> locationService
                        .save(LOCATION_WEATHER_DTO.name(), LOCATION_WEATHER_DTO.latitude(), LOCATION_WEATHER_DTO.longitude(), existingUserId)),
                () -> assertThrows(EntityNotFoundException.class, () -> locationService.save("any()", BigDecimal.ONE, BigDecimal.ONE, nonExistingUserId))
        );
    }


    @Test
    public void userDeletesHisOwnLocation() throws UnauthorizedException, EntityNotFoundException {
        Long userId = 1L;
        Long locationId = 1L;
        locationService.delete(locationId, userId);
    }

    @Test
    public void userDeletesOtherUserLocationCausingException(){
        Long userId = 2L;
        Long locationId = 1L;
        assertThrows(UnauthorizedException.class, () -> locationService.delete(locationId, userId));
    }
}
