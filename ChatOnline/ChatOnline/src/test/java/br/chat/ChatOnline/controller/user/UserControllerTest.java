package br.chat.ChatOnline.controller.user;

import br.chat.ChatOnline.dto.auth.AuthenticationDTO;
import br.chat.ChatOnline.dto.auth.TokenJwtDTO;
import br.chat.ChatOnline.models.user.User;
import br.chat.ChatOnline.repository.user.IUserRepository;
import br.chat.ChatOnline.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<AuthenticationDTO> jsonRequest;

    @Autowired
    private JacksonTester<TokenJwtDTO> jsonResponse;

    @MockBean
    private UserService userService;

    @MockBean
    private IUserRepository iUserRepository;

    @Test
    @DisplayName("Should return http code 400 when input data is invalid")
    @WithMockUser
    void registerInvalidData() throws Exception {
        var response = mockMvc.perform(post("/users"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Should return http code 201 and User when input data is valid")
    @WithMockUser
    void registerValidData() throws Exception {
        var password = "senha";
        var hashedPassword = "hashedSenha";
        var authenticationDTO = new AuthenticationDTO("userName", password);
        var user = new User(authenticationDTO, hashedPassword);

        when(userService.passwordHash(password)).thenReturn(hashedPassword);
        when(iUserRepository.save(any(User.class))).thenReturn(user);

        var response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest.write(authenticationDTO).getJson()))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var jsonExpected = jsonRequest.write(authenticationDTO).getJson();
        assertThat(response.getContentAsString()).contains(jsonExpected);
    }

}
