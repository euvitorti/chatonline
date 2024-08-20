package br.chat.ChatOnline.controller.user;

import br.chat.ChatOnline.dto.auth.AuthenticationDTO;
import br.chat.ChatOnline.models.user.User;
import br.chat.ChatOnline.repository.user.IUserRepository;
import br.chat.ChatOnline.service.user.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid AuthenticationDTO authenticationDTO, UriComponentsBuilder uriComponentsBuilder){
        String hashedPassword = userService.passwordHash(authenticationDTO.password());
        var user = new User(authenticationDTO, hashedPassword);
        iUserRepository.save(user);

        var uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).body(user);
    }

}
