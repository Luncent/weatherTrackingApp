package org.example.services;

import lombok.AllArgsConstructor;
import org.example.entities.User;
import org.example.exceptions.EntityNotFoundException;
import org.example.repositories.repos_impl.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    public User save(String login, String s) {
        return userRepository.save(
                User.builder()
                        .login(login)
                        .password(s)
                        .build()
        );
    }

    public User findByLogin(String login) throws EntityNotFoundException {
        Optional<User> userOptional = userRepository.findByLogin(login);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return userOptional.get();
    }

}
