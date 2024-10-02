package br.chat.ChatOnline.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Service annotation indicates that this class provides business logic
@Service
public class UserService {

    // Injecting the PasswordEncoder to handle password encoding
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Constructor for UserService to initialize the PasswordEncoder
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Method to hash a plain text password
    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword); // Encoding the password and returning the hashed value
    }
}
