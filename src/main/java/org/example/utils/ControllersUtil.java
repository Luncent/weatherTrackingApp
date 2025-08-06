package org.example.utils;

import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class ControllersUtil {
    public static String getErrorsMessages(BindingResult bindingResult){
        return bindingResult.getAllErrors().stream()
                .map(err->err.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }
}
