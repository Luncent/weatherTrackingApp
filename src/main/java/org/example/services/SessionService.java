package org.example.services;

import lombok.extern.log4j.Log4j2;
import org.example.entities.HttpSession;
import org.example.entities.User;
import org.example.exceptions.NoAvailableSessionException;
import org.example.repositories.repos_impl.HttpSessionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public class SessionService {
    private final Duration sessionDuration;
    private final HttpSessionRepository httpSessionRepository;

    public SessionService(HttpSessionRepository httpSessionRepository,
                          @Value("${session_duration_sec}") int sessionDuration) {
        this.httpSessionRepository = httpSessionRepository;
        this.sessionDuration = Duration.ofSeconds(sessionDuration);
    }

    public HttpSession findByIdAndCheckActive(UUID id) throws NoAvailableSessionException {
        Optional<HttpSession> sessionOpt = httpSessionRepository.getById(id);
        if (sessionOpt.isEmpty() || !isSessionActive(sessionOpt.get())) {
            throw new NoAvailableSessionException();
        }
        return sessionOpt.get();
    }

    public HttpSession openSessionForUser(User user) {
        return httpSessionRepository.save(
                HttpSession.builder()
                        .id(UUID.randomUUID())
                        .expiresAt(countExpirationTime())
                        .user(user)
                        .build()
        );
    }

    public void deleteById(UUID sessionID){
        httpSessionRepository.delete(sessionID);
    }

    public boolean isSessionActive(HttpSession session) {
        LocalDateTime now = LocalDateTime.now();
        log.debug("now time {} and session expires at {}",now,session.getExpiresAt());
        return session.getExpiresAt().isAfter(now);
    }

    private LocalDateTime countExpirationTime() {
        return LocalDateTime.now().plus(sessionDuration);
    }
}
