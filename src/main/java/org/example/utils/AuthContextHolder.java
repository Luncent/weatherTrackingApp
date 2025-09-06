package org.example.utils;

import lombok.extern.log4j.Log4j2;
import org.example.model.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Log4j2
public class AuthContextHolder {
    private static final ThreadLocal<Optional<Authentication>> authContextHolder = new ThreadLocal<>();

    public static void setAuthContext(Optional<Authentication> auth) {
        authContextHolder.set(auth);
    }

    public static Optional<Authentication> getAuthentication() {
        return authContextHolder.get();
    }

    public static void cleanUp(){
        log.debug("cleaning up authContextHolder");
        authContextHolder.remove();
    }
}
