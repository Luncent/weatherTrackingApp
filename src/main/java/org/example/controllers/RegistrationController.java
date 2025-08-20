package org.example.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.controllers.cookies.CookieHandler;
import org.example.dto.user.UserRegistrationDTO;
import org.example.exception_handling.exceptions.repository.EntityExistsException;
import org.example.services.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

import static org.example.utils.ControllersUtil.getErrorsMessages;

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
    public String signUp(@Validated UserRegistrationDTO newUser, BindingResult validationResult,
                         HttpServletResponse response, RedirectAttributes redirectAttributes) {
        if (validationResult.hasErrors()) {
            log.debug(getErrorsMessages(validationResult));
            redirectAttributes.addFlashAttribute("errors", getErrorsMessages(validationResult));
            return "redirect:/app/registration";
        }
        UUID sessionId = registrationService.register(newUser.getLogin(), newUser.getPassword());
        cookieHandler.setSessionCookie(response, sessionId);
        return "redirect:/app/";
    }
}
