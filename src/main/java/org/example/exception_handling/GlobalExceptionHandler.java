package org.example.exception_handling;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.example.entities.Location;
import org.example.entities.User;
import org.example.exception_handling.exceptions.repository.DBException;
import org.example.exception_handling.exceptions.repository.EntityExistsException;
import org.example.exception_handling.exceptions.service.AuthException;
import org.example.exception_handling.exceptions.weather_api.WeatherApiException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.example.utils.ControllersUtil.getErrorsMessages;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {


    //TODO redirect on error in all methods otherwise browsel url not /error?
    @ExceptionHandler(WeatherApiException.class)
    public ModelAndView handleWeatherApiException(final WeatherApiException weatherApiException) {
        log.error(weatherApiException.getMessage(), weatherApiException);
        return supplyModelAndView("error", "Error with weather API");
    }

    @ExceptionHandler(EntityExistsException.class)
    public ModelAndView handleEntityExistsException(final EntityExistsException entityExistsException) {
        log.error(entityExistsException.getMessage(), entityExistsException);
        return supplyModelAndView("error", entityExistsException.getMessage()+" already exists");
    }

    @ExceptionHandler(DBException.class)
    public ModelAndView handleDBException(final DBException dbException) {
        log.error(dbException.getMessage(), dbException);
        return new ModelAndView("error");
    }

    @ExceptionHandler(AuthException.class)
    public ModelAndView handleAuthException(final AuthException authException, RedirectAttributes redirectAttributes) {
        log.error(authException.getMessage(), authException);
        redirectAttributes.addFlashAttribute("errors", authException.getMessage());
        return switch(authException.getErrorType()){
            case LOGIN -> new ModelAndView("redirect:/app/login");
            case REGISTER -> new ModelAndView("redirect:/app/registration");
        };
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleEntityNotFoundException(final EntityNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return supplyModelAndView("error", ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleException(final Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
        return supplyModelAndView("error", " shit something went wrong");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex,
                                                              RedirectAttributes redirectAttributes) {
        log.error("validation errors ", ex);
        redirectAttributes.addFlashAttribute("errors", getErrorsMessages(ex.getAllErrors()));
        String rejectedDtoName = ex.getObjectName();
        String page = switch(rejectedDtoName){
            case "userLoginDTO" -> "redirect:/app/login";
            case "userRegistrationDTO" -> "redirect:/app/registration";
            default -> "redirect:/app/error";
        };
        return new ModelAndView(page);
    }

    private ModelAndView supplyModelAndView(String viewName, String errorMessage) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", errorMessage);
        modelAndView.setViewName(viewName);
        return modelAndView;
    }
}
