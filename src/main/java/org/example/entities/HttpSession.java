package org.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="sessions")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HttpSession {

    @Id
    private UUID id;

    @Column(name="expires_at")
    private LocalDateTime expiresAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
