package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.example.controllers.cookies.CookieHandler;
import org.example.dto.UserDTO;
import org.example.dto.requests_dtos.UserRegistrationDTO;
import org.example.services.RegistrationService;
import org.example.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final CookieHandler cookieHandler;

    @GetMapping
    public String getPage(){
        return "sign_up";
    }

    @PostMapping
    @SneakyThrows
    public String signUp(UserRegistrationDTO newUser, HttpServletResponse response){
        UUID sessionId = registrationService
                .register(newUser.getLogin(), newUser.getPassword(), newUser.getPasswordConfirm());
        cookieHandler.setSessionCookie(response, sessionId);
        return "redirect:/";
    }
}
