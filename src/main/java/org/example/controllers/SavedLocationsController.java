package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.example.controllers.cookies.CookieHandler;
import org.example.exceptions.NoAvailableSessionException;
import org.example.services.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping({"/saved_locations", "/"})
@AllArgsConstructor
public class SavedLocationsController {

    private final CookieHandler cookieHandler;
    private final SessionService sessionService;

    @GetMapping
    public String getPage(Model model, HttpServletRequest request){
        String username = null;
        try {
            UUID sessionId = cookieHandler.getSessionCookie(request);
            username = sessionService.findById(sessionId).getUser().getLogin();
        }catch (NoAvailableSessionException e) {
            return "savedLocations";
        }
        model.addAttribute("username", username);
        return "savedLocations";
    }



}
