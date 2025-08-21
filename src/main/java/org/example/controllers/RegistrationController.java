package org.example.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.dto.user.UserRegistrationDTO;
import org.example.services.RegistrationService;
import org.example.utils.CookieHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("app/registration")
@AllArgsConstructor
@Log4j2
public class RegistrationController {

    private final RegistrationService registrationService;
    private final CookieHandler cookieHandler;

    @GetMapping
    public String getPage() {
        return "sign_up";
    }

    @PostMapping
    public String signUp(@Validated UserRegistrationDTO newUser, HttpServletResponse response) {
        UUID sessionId = registrationService.register(newUser.getLogin(), newUser.getPassword());
        cookieHandler.setSessionCookie(response, sessionId);
        return "redirect:/app";
    }
}
