package br.chat.ChatOnline.controller.auth;

import br.chat.ChatOnline.dto.auth.AuthenticationDTO;
import br.chat.ChatOnline.dto.auth.TokenJwtDTO;
import br.chat.ChatOnline.infra.security.TokenJwt;
import br.chat.ChatOnline.models.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<AuthenticationDTO> jsonRequest;

    @Autowired
    private JacksonTester<TokenJwtDTO> jsonResponse;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private TokenJwt tokenJwt;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should return HTTP 200 and token when login is successful")
    void loginSuccessful() throws Exception {
        var authenticationDTO = new AuthenticationDTO("userName", "password");
        var user = new User(); // Supondo que o construtor padrão está disponível
        var authToken = new UsernamePasswordAuthenticationToken(authenticationDTO.userName(), authenticationDTO.password());
        var auth = Mockito.mock(Authentication.class);

        when(authenticationManager.authenticate(authToken)).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(user);
        when(tokenJwt.generateToken(user)).thenReturn("fake-jwt-token");

        var response = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest.write(authenticationDTO).getJson()))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        var expectedResponse = jsonResponse.write(new TokenJwtDTO("fake-jwt-token")).getJson();
        assertThat(response.getContentAsString()).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should return HTTP 400 when login fails")
    void loginFailed() throws Exception {
        var authenticationDTO = new AuthenticationDTO("userName", "wrongPassword");
        var authToken = new UsernamePasswordAuthenticationToken(authenticationDTO.userName(), authenticationDTO.password());

        when(authenticationManager.authenticate(authToken)).thenThrow(new BadCredentialsException("Invalid credentials"));

        var response = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest.write(authenticationDTO).getJson()))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("Invalid credentials");
    }
}
