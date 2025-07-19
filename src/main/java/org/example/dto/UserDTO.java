package org.example.dto;

import java.util.Optional;
import java.util.UUID;

public record UserDTO(Long id, String login, Optional<UUID> sessionId) {
}
