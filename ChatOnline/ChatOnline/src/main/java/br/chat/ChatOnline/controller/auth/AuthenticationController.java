package br.chat.ChatOnline.controller.auth;

import br.chat.ChatOnline.dto.auth.AuthenticationDTO;
import br.chat.ChatOnline.dto.auth.TokenJwtDTO;
import br.chat.ChatOnline.infra.security.TokenJwt;
import br.chat.ChatOnline.models.user.User;
import br.chat.ChatOnline.repository.user.IUserRepository;
import br.chat.ChatOnline.service.user.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

// CLASSE CONTROLLER NÃO DEVE TER REGRAS DE NOGÓCIO DA APLICAÇÃO

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    // ESTA É CLASSE QUE DISPARA O PROCESSO DE AUTENTICAÇÃO
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenJwt tokenJwt;

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(authenticationDTO.userName(), authenticationDTO.password());
            var auth = authenticationManager.authenticate(authToken);

            // PASSANDO O USUÁRIO COMO PARÂMETRO
            var token = tokenJwt.generateToken((User) auth.getPrincipal());
            return ResponseEntity.ok(new TokenJwtDTO(token));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @PostMapping
//    @Transactional
//    public ResponseEntity register(@RequestBody @Valid AuthenticationDTO authenticationDTO, UriComponentsBuilder uriComponentsBuilder){
//        String hashedPassword = userService.passwordHash(authenticationDTO.password());
//        var user = new User(authenticationDTO, hashedPassword);
//        iUserRepository.save(user);
//
//        var uri = uriComponentsBuilder.path("/login/{id}").buildAndExpand(user.getId()).toUri();
//        var login = login(authenticationDTO);
//        return ResponseEntity.created(uri).body(login);
//    }

}
