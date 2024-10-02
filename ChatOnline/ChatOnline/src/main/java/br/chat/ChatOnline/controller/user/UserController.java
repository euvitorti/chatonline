package br.chat.ChatOnline.controller.user;

import br.chat.ChatOnline.controller.auth.AuthenticationController;
import br.chat.ChatOnline.dto.auth.AuthenticationDTO;
import br.chat.ChatOnline.models.user.User;
import br.chat.ChatOnline.repository.user.IUserRepository;
import br.chat.ChatOnline.service.user.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository iUserRepository; // Repository for user data

    @Autowired
    private UserService userService; // Service for user-related operations

    // Endpoint for user registration
    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid AuthenticationDTO authenticationDTO, UriComponentsBuilder uriComponentsBuilder){
        // Hash the provided password
        String hashedPassword = userService.hashPassword(authenticationDTO.password());
        // Create a new User object
        var user = new User(authenticationDTO, hashedPassword);
        // Save the new user in the repository
        iUserRepository.save(user);

        // Build the URI for the created user
        var uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();

        // Return a response indicating that the user has been created, along with the user details
        return ResponseEntity.created(uri).body(new AuthenticationDTO(authenticationDTO.userName(), authenticationDTO.password()));
    }
}
