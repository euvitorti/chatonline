package br.chat.ChatOnline.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void PasswordService() {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public String hashPassword(String rawPassword) {
        return bCryptPasswordEncoder.encode(rawPassword);
    }
}
