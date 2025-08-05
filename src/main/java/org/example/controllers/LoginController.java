package org.example.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.example.controllers.cookies.CookieHandler;
import org.example.dto.requests_dtos.UserLoginDTO;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.LoginService;
import org.example.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/login")
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final CookieHandler cookieHandler;

    @GetMapping
    public String getPage() {
        return "login";
    }

    @PostMapping
    public String login(@Validated UserLoginDTO loginDTO, HttpServletResponse response) throws EntityNotFoundException {
        UUID sessionId = loginService.login(loginDTO.getLogin(), loginDTO.getPassword());
        cookieHandler.setSessionCookie(response, sessionId);
        return "redirect:/saved_locations";
    }
}
