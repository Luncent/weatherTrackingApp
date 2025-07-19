package org.example.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.example.entities.HttpSession;
import org.example.entities.User;
import org.example.repositories.repos_impl.HttpSessionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {

    private final Duration duration;

    private final HttpSessionRepository httpSessionRepository;

    public SessionService(HttpSessionRepository httpSessionRepository,
                          @Value("${session_duration_sec}") int duration) {
        this.httpSessionRepository = httpSessionRepository;
        this.duration = Duration.ofSeconds(duration);
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

    private LocalDateTime countExpirationTime(){
        return LocalDateTime.now().plus(duration);
    }

    public boolean isSessionActive(UUID uuid) {
        Optional<HttpSession> session = httpSessionRepository.getById(uuid);
        if(session.isEmpty()){
            throw new EntityNotFoundException();
        }
        return  session.get().getExpiresAt().isAfter(LocalDateTime.now());
    }
}
