package org.example.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.dto.locations.LocationPageDTO;
import org.example.dto.locations.LocationSaveDTO;
import org.example.model.Authentication;
import org.example.model.Coordinate;
import org.example.services.LocationService;
import org.example.services.WeatherAPIService;
import org.example.utils.AuthContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.BiConsumer;

import static java.net.URLEncoder.encode;

@Controller
@RequestMapping("app")
@AllArgsConstructor
@Log4j2
public class LocationsController {

    private static final Integer DEFAULT_PAGE_NUMBER = 1;

    private final LocationService locationService;
    private final WeatherAPIService weatherAPIService;

    @GetMapping
    public String myLocations(@RequestParam(required = false, name = "currentPage") Integer currentPage,
                              Model model){
        fillModelWithUsernameAndSpecificData(model, (model2, auth)  -> {
            LocationPageDTO page = locationService.selectPaginated(currentPage==null ? DEFAULT_PAGE_NUMBER : currentPage, auth.getId());
            model2.addAttribute("myLocations", page);
        });
        return "my_locations";
    }

    @GetMapping("/locations/search")
    public String searchLocations(@RequestParam("city") String city, Model model){
        fillModelWithUsernameAndSpecificData(model, null);
        model.addAttribute("resultCities", weatherAPIService.searchByCityName(city));
        model.addAttribute("searchingCity", city);
        return "search_locations";
    }

    @PostMapping("/locations/add")
    public String save(LocationSaveDTO location, @RequestParam("searchVal") String searchVal,
                       Model model) {
        fillModelWithUsernameAndSpecificData(model, (model2, auth) -> locationService.save(location, auth.getId()));
        return "redirect:/app/locations/search?city="+ encode(searchVal, StandardCharsets.UTF_8);
    }

    @PostMapping("/locations/delete")
    public String delete(Coordinate coordinate,
                         @RequestParam("currentPage") Integer currentPage){
        locationService.delete(coordinate, AuthContextHolder.getAuthentication().get().getId());
        return "redirect:/app?currentPage="+currentPage;
    }

    private void fillModelWithUsernameAndSpecificData(Model model, BiConsumer<Model, Authentication> dataFillingAction) {
        Optional<Authentication> optionalAuth = AuthContextHolder.getAuthentication();
        if(optionalAuth.isPresent()){
            Authentication auth = optionalAuth.get();
            model.addAttribute("username", auth.getUsername());
            if(dataFillingAction!=null) dataFillingAction.accept(model, auth);
        }
    }

}
