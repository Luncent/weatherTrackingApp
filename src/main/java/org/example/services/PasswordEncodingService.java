package org.example.services;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncodingService {

    public String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean isSamePassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
