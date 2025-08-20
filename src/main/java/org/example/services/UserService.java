package org.example.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.example.entities.User;
import org.example.repositories.repos_impl.UserRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User save(String login, String password) {
        return userRepository.save(
                User.builder()
                        .login(login)
                        .password(password)
                        .build()
        );
    }

    public User findByLogin(String login){
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
