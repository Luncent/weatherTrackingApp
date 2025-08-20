package org.example.utils;

import lombok.extern.log4j.Log4j2;
import org.example.model.Authorization;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Log4j2
public class AuthContextHolder {
    private static ThreadLocal<Optional<Authorization>> authContextHolder = new ThreadLocal<>();

    public static void setAuthContext(Optional<Authorization> auth) {
        authContextHolder.set(auth);
    }

    public static Optional<Authorization> getAuthContext() {
        return authContextHolder.get();
    }

    public static void cleanUp(){
        log.debug("cleaning up authContextHolder");
        authContextHolder.remove();
    }
}
