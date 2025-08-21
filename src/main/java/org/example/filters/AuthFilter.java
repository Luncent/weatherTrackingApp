package org.example.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.entities.User;
import org.example.exception_handling.exceptions.NoAvailableSessionException;
import org.example.model.Authentication;
import org.example.services.SessionService;
import org.example.utils.AuthContextHolder;
import org.example.utils.CookieHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@AllArgsConstructor
@Log4j2
public class AuthFilter implements Filter {

    private static final Set<String> URLS_WITH_OPTIONAL_AUTH = new HashSet<>();
    private static final Set<String> URLS_WITHOUT_AUTHORIZATION = new HashSet<>();
    private static final Set<String> AUTHORIZED_URLS = new HashSet<>();

    static {
        URLS_WITH_OPTIONAL_AUTH.add("/app");
        URLS_WITH_OPTIONAL_AUTH.add("/app/locations/search.*");

        AUTHORIZED_URLS.add("/app/locations/add");
        AUTHORIZED_URLS.add("/app/locations/delete");

        URLS_WITHOUT_AUTHORIZATION.add("/app/login");
        URLS_WITHOUT_AUTHORIZATION.add("/app/registration");
        URLS_WITHOUT_AUTHORIZATION.add("/app/sign_out");
        URLS_WITHOUT_AUTHORIZATION.add("/app/error.*");
    }

    private final CookieHandler cookieHandler;
    private final SessionService sessionService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        for(String url : URLS_WITHOUT_AUTHORIZATION) {
            if(request.getRequestURI().matches(request.getContextPath()+url)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        for(String url : AUTHORIZED_URLS) {
            if(request.getRequestURI().matches(request.getContextPath()+url)) {
                try {
                    produceAuthorization(request);
                    filterChain.doFilter(request, response);
                    return;
                } catch (NoAvailableSessionException e) {
                    log.debug("tying to access secured uri: {} without active session", request.getRequestURI());
                    String contextPath = request.getContextPath();
                    String redirectPath = contextPath+"/app/login";
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.sendRedirect(redirectPath);
                    return;
                }
            }
        }


        for(String url : URLS_WITH_OPTIONAL_AUTH) {
            if (request.getRequestURI().matches(request.getContextPath()+url)) {

                try {
                    produceAuthorization(request);
                    filterChain.doFilter(request, response);
                    return;
                } catch (NoAvailableSessionException e) {
                    AuthContextHolder.setAuthContext(Optional.empty());
                    log.debug("{} session not found", request.getRequestURI());
                    filterChain.doFilter(request, response);
                    return;
                }

            }
        }


        response.setStatus(HttpStatus.NOT_IMPLEMENTED.value());
        String errorMessage = URLEncoder.encode("Not Implemented", UTF_8);
        response.sendRedirect(request.getContextPath()+"/app/error?message="+errorMessage);

    }

    private void produceAuthorization(HttpServletRequest request) throws NoAvailableSessionException, IOException, ServletException {
        UUID sessionId = cookieHandler.getSessionCookie(request);
        User user = sessionService.findByIdAndCheckActive(sessionId).getUser();
        Authentication authentication = Authentication.builder()
                .id(user.getId())
                .username(user.getLogin())
                .build();
        AuthContextHolder.setAuthContext(Optional.of(authentication));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
