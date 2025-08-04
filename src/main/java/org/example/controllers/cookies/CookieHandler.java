package org.example.controllers.cookies;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.UserDTO;
import org.example.exceptions.NoAvailableSessionException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Component
public class CookieHandler {
    private static final String SESSION_COOKIE_NAME = "sessionId";

    public void setSessionCookie(final HttpServletResponse response, UUID sessionId) {
        Cookie sessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId.toString());
        response.addCookie(sessionCookie);
    }

    public UUID getSessionCookie(final HttpServletRequest request) throws NoAvailableSessionException {
        Optional<Cookie> sessionIdOpt = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(SESSION_COOKIE_NAME))
                .findFirst();
        if (sessionIdOpt.isEmpty()) {
            throw new NoAvailableSessionException();
        }
        return UUID.fromString(sessionIdOpt.get().getValue());
    }
}
