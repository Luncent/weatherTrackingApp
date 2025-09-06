package integration;

import annotations.IT;
import jakarta.persistence.EntityNotFoundException;
import org.example.entities.Location;
import org.example.exception_handling.exceptions.repository.EntityExistsException;
import org.example.exception_handling.exceptions.UnauthorizedException;
import org.example.model.Coordinate;
import org.example.services.LocationService;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static java.lang.Double.parseDouble;
import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static test_constants.LocationDTOConstants.LOCATION_SAVE_DTO_DTO;

@IT
public class LocationServiceTest {

    private final static BigDecimal LATITUDE = valueOf(parseDouble("51.5073219"));
    private final static BigDecimal LONGITUDE = valueOf(parseDouble("-0.1276474"));
    private final static Coordinate COORDINATE = new Coordinate(LATITUDE, LONGITUDE);
    private final static Long EXISTING_LOCATION_ID_USER_1 = 1L;

    @Autowired
    LocationService locationService;

    @Autowired
    SessionFactory sessionFactory;

    @Test
    public void saveNewLocationSuccess() throws EntityNotFoundException, EntityExistsException {
        Long userId = 1L;

        Location location = locationService
                .save(LOCATION_SAVE_DTO_DTO, userId);
        Long savedLocationId = location.getId();

        sessionFactory.getCurrentSession().detach(location);

        location = null;

        location = locationService.findById(savedLocationId);

        assertThat(location.getName()).isEqualTo(LOCATION_SAVE_DTO_DTO.getName());
    }

    @Test
    public void saveLocationWhenUserAlreadySavedItOrUserNotExists() throws EntityNotFoundException, EntityExistsException {
        Long existingUserId = 1L;
        Long nonExistingUserId = -12L;

        locationService.save(LOCATION_SAVE_DTO_DTO, existingUserId);

        assertAll(
                () -> assertThrows(EntityExistsException.class, () -> locationService
                        .save(LOCATION_SAVE_DTO_DTO, existingUserId)),
                () -> assertThrows(EntityNotFoundException.class, () -> locationService.save(LOCATION_SAVE_DTO_DTO, nonExistingUserId))
        );
    }

    @Test
    public void userDeletesHisOwnLocation() throws EntityNotFoundException {
        Long userId = 1L;
        assertDoesNotThrow(()->locationService.delete(COORDINATE, userId));
    }

    @Test
    public void userDeletesOtherUserLocationCausingException(){
        Long userId = 2L;
        assertThrows(EntityNotFoundException.class, () -> locationService.delete(COORDINATE, userId));
    }
}
