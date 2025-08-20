package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.controllers.cookies.CookieHandler;
import org.example.dto.locations.LocationPageDTO;
import org.example.dto.locations.LocationSaveDTO;
import org.example.entities.User;
import org.example.exception_handling.exceptions.NoAvailableSessionException;
import org.example.model.Coordinate;
import org.example.services.LocationService;
import org.example.services.SessionService;
import org.example.services.WeatherAPIService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static java.net.URLEncoder.encode;

@Controller
@RequestMapping("app")
@AllArgsConstructor
@Log4j2
public class LocationsController {

    private final CookieHandler cookieHandler;
    private final SessionService sessionService;
    private final LocationService locationService;
    private final WeatherAPIService weatherAPIService;


    @GetMapping
    public String myLocations(@RequestParam(required = false, name = "currentPage") Integer currentPage,
                              Model model, HttpServletRequest request){
        try {
            UUID sessionId = cookieHandler.getSessionCookie(request);
            User user = sessionService.findByIdAndCheckActive(sessionId).getUser();

            LocationPageDTO page = locationService.selectPaginated(currentPage==null ? 1 : currentPage, user.getId());
            System.out.println(page.locationWeatherDTOList().size());
            model.addAttribute("myLocations", page);
            model.addAttribute("username", user.getLogin());
        }catch (NoAvailableSessionException e) {
            log.debug("{} session not found", request.getRequestURI());
            return "my_locations";
        }

        return "my_locations";
    }

    @GetMapping("/locations/search")
    public String searchLocations(@RequestParam("city") String city, Model model, HttpServletRequest request){
        try {
            UUID sessionId = cookieHandler.getSessionCookie(request);
            String username = sessionService.findByIdAndCheckActive(sessionId).getUser().getLogin();
            model.addAttribute("username", username);
        }catch (NoAvailableSessionException e) {
            log.debug("{} session not found", request.getRequestURI());
        }

        model.addAttribute("resultCities", weatherAPIService.searchByCityName(city));
        model.addAttribute("searchingCity", city);
        return "search_locations";
    }

    //TODO show error message if location already saved
    @PostMapping("/locations")
    public String save(LocationSaveDTO location, @RequestParam("searchVal") String searchVal,
                       Model model, HttpServletRequest request) {
        try {
            UUID sessionId = cookieHandler.getSessionCookie(request);
            User user = sessionService.findByIdAndCheckActive(sessionId).getUser();
            model.addAttribute("username", user.getLogin());
            locationService.save(location, user.getId());
        }catch (NoAvailableSessionException e) {
            log.debug("{} session not found", request.getRequestURI());
        }

        return "redirect:/app/locations/search?city="+ encode(searchVal, StandardCharsets.UTF_8);
    }

    @PostMapping("/locations/delete")
    public String delete(Coordinate coordinate,
                         @RequestParam("currentPage") Integer currentPage,
                         HttpServletRequest request){
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
