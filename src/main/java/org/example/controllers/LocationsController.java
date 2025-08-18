package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.controllers.cookies.CookieHandler;
import org.example.dto.LocationPageDTO;
import org.example.dto.LocationWeatherDTO;
import org.example.dto.requests_dtos.LocationSaveDTO;
import org.example.entities.User;
import org.example.exceptions.EntityExistsException;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.NoAvailableSessionException;
import org.example.model.Coordinate;
import org.example.services.LocationService;
import org.example.services.SessionService;
import org.example.services.WeatherAPIService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

import static java.util.List.of;

@Controller
@RequestMapping("app")
@AllArgsConstructor
@Log4j2
public class LocationsController {

    private final CookieHandler cookieHandler;
    private final SessionService sessionService;
    private final LocationService locationService;
    private final WeatherAPIService weatherAPIService;


   /* private LocationPageDTO mockLocationPageDTO(Integer currentPage){
        LocationWeatherDTO locationWeatherDTO = new LocationWeatherDTO(
                1L,
                BigDecimal.ONE,
                BigDecimal.ONE,
                2,
                BigDecimal.ONE,
                BigDecimal.ONE,
                "BY",
                "snow",
                "Zhodino",
                "13n"
        );
        LocationWeatherDTO locationWeatherDTO2 = new LocationWeatherDTO(
                2L,
                BigDecimal.ONE,
                BigDecimal.ONE,
                2,
                BigDecimal.ONE,
                BigDecimal.ONE,
                "BY",
                "few clouds",
                "Zhodino",
                "02d"
        );
        return new LocationPageDTO(of(locationWeatherDTO,locationWeatherDTO2), currentPage, 10L);
    }*/


    @GetMapping
    public String myLocations(@RequestParam(required = false, name = "currentPage") Integer currentPage,
                              Model model, HttpServletRequest request) throws Exception {
        try {
            UUID sessionId = cookieHandler.getSessionCookie(request);
            User user = sessionService.findByIdAndCheckActive(sessionId).getUser();

            LocationPageDTO page = locationService.selectPaginated(currentPage==null?1:currentPage, user.getId());
            System.out.println(page.locationWeatherDTOList().size());
            model.addAttribute("myLocations", page);
            model.addAttribute("username", user.getLogin());
        }catch (NoAvailableSessionException e) {
            log.debug("{} session not found", request.getRequestURI());
            return "my_locations";
        }

        //LocationPageDTO page = mockLocationPageDTO(currentPage==null?1:currentPage);

        return "my_locations";
    }

    @GetMapping("/locations/search")
    public String locations(@RequestParam("city") String city, Model model, HttpServletRequest request) throws Exception {
        try {
            UUID sessionId = cookieHandler.getSessionCookie(request);
            String username = sessionService.findByIdAndCheckActive(sessionId).getUser().getLogin();
            model.addAttribute("username", username);
        }catch (NoAvailableSessionException e) {
            log.debug("{} session not found", request.getRequestURI());
        }

        model.addAttribute("resultCities", weatherAPIService.getLocationsByCityName(city));
        model.addAttribute("searchingCity", city);
        return "search_locations";
    }

    //TODO show error message if location already saved
    @PostMapping("/locations")
    public String save(LocationSaveDTO location, @RequestParam("searchVal") String searchVal,
                       Model model, HttpServletRequest request) throws EntityNotFoundException, EntityExistsException {
        try {
            UUID sessionId = cookieHandler.getSessionCookie(request);
            User user = sessionService.findByIdAndCheckActive(sessionId).getUser();
            model.addAttribute("username", user.getLogin());
            locationService.save(location.getName(),location.getLatitude(),location.getLongitude(), user.getId());
        }catch (NoAvailableSessionException e) {
            log.debug("{} session not found", request.getRequestURI());
        }

        return "redirect:/app/locations/search?city="+searchVal;
    }

    @PostMapping("/locations/delete")
    public String delete(Coordinate coordinate,
                         @RequestParam("currentPage") Integer currentPage,
                         HttpServletRequest request) throws EntityNotFoundException {
        try {
            UUID sessionId = cookieHandler.getSessionCookie(request);
            User user = sessionService.findByIdAndCheckActive(sessionId).getUser();
            locationService.delete(coordinate, user.getId());
        }catch (NoAvailableSessionException e) {
            log.debug("{} session not found", request.getRequestURI());
        }

        return "redirect:/app?currentPage="+currentPage;
    }
}
