package org.example.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.utils.AuthContextHolder;
import org.example.utils.CookieHandler;
import org.example.dto.user.UserLoginDTO;
import org.example.services.LoginService;
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
@RequestMapping("app/login")
@AllArgsConstructor
@Log4j2
public class LoginController {

    private final LoginService loginService;
    private final CookieHandler cookieHandler;

    @GetMapping
    public String getPage() {
        return "login";
    }

    @PostMapping
    public String login(@Validated UserLoginDTO loginDTO, BindingResult validationResult,
                        HttpServletResponse response, RedirectAttributes redirectAttributes) {

        log.debug(AuthContextHolder.getAuthContext());

        if (validationResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", getErrorsMessages(validationResult));
            log.debug("validation errors {}", getErrorsMessages(validationResult));
            return "redirect:/app/login";
        }
        UUID sessionId = loginService.login(loginDTO.getLogin(), loginDTO.getPassword());
        cookieHandler.setSessionCookie(response, sessionId);
        return "redirect:/app";
    }

}
