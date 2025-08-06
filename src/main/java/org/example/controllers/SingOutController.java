package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.controllers.cookies.CookieHandler;
import org.example.exceptions.NoAvailableSessionException;
import org.example.services.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/sign_out")
@AllArgsConstructor
@Log4j2
public class SingOutController {
    private final CookieHandler cookieHandler;
    private final SessionService sessionService;

    @PostMapping
    public String singOut(HttpServletRequest request, HttpServletResponse response){
        try {
            UUID sessionId = cookieHandler.removeSessionCookie(request, response);
            sessionService.deleteById(sessionId);
            log.debug("signed out successfully");
        }
        catch (NoAvailableSessionException ex){
            log.debug("sign out: session cookie not found");
        }
        catch (Exception ex){
            log.error(ex.getMessage());
        }
        return "redirect:/login";
    }
}
