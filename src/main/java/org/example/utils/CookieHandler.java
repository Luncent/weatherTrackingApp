package org.example.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception_handling.exceptions.NoAvailableSessionException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static java.util.Arrays.stream;

@Component
public class CookieHandler {
    private static final String SESSION_COOKIE_NAME = "sessionId";

    public void setSessionCookie(final HttpServletResponse response, UUID sessionId) {
        Cookie sessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId.toString());
        response.addCookie(sessionCookie);
    }

    public UUID getSessionCookie(final HttpServletRequest request) throws NoAvailableSessionException {
        Cookie[] cookies = request.getCookies();
        if(cookies==null){
            throw new NoAvailableSessionException();
        }
        Optional<Cookie> sessionIdOpt = stream(cookies)
                .filter(cookie -> cookie.getName().equals(SESSION_COOKIE_NAME))
                .findFirst();
        if (sessionIdOpt.isEmpty()) {
            throw new NoAvailableSessionException();
        }
        return UUID.fromString(sessionIdOpt.get().getValue());
    }

    public UUID removeSessionCookie(HttpServletRequest request, HttpServletResponse response) throws NoAvailableSessionException {
        UUID sessionID = getSessionCookie(request);

        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, "dummy");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return sessionID;
    }

}
