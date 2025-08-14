package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.controllers.cookies.CookieHandler;
import org.example.dto.requests_dtos.LocationSaveDTO;
import org.example.exceptions.NoAvailableSessionException;
import org.example.services.LocationService;
import org.example.services.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;

@Controller
@RequestMapping("/")
@AllArgsConstructor
@Log4j2
public class LocationsController {

    private final CookieHandler cookieHandler;
    private final SessionService sessionService;
    private final LocationService locationService;

    @GetMapping
    public String myLocations(Model model, HttpServletRequest request) {
        String username = null;
        try {
            UUID sessionId = cookieHandler.getSessionCookie(request);
            username = sessionService.findById(sessionId).getUser().getLogin();
        }catch (NoAvailableSessionException e) {
            return "my_locations";
        }
        model.addAttribute("username", username);
        return "my_locations";
    }

    @GetMapping("/locations/search")
    public String locations(@RequestParam("city") String city, Model model, HttpServletRequest request) {
        try {
            UUID sessionId = cookieHandler.getSessionCookie(request);
            String username = sessionService.findById(sessionId).getUser().getLogin();
            model.addAttribute("username", username);
        }catch (NoAvailableSessionException e) {
            log.debug("/locations/search session not found: continue search");
        }

        model.addAttribute("searchingCity", city);
        return "/search_locations";
    }

    //TODO add message if location already saved
    @PostMapping("/locations")
    public String save(LocationSaveDTO location, @RequestParam("searchVal") String searchVal, Model model) {
        return "redirect:locations/search?city="+searchVal;
    }

    @DeleteMapping("locations/{id}")
    public String delete(@PathVariable int id) {
        return "redirect:/";
    }
}
