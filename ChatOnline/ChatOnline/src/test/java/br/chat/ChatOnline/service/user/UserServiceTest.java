package br.chat.ChatOnline.service.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        // Inicializa os mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve codificar a senha corretamente")
    void shouldEncodePassword() {
        String rawPassword = "myPassword";
        String encodedPassword = "encodedPassword";

        // Configuração do mock
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        // Chama o método a ser testado
        String result = userService.hashPassword(rawPassword);

        // Verifica o resultado
        assertEquals(encodedPassword, result, "A senha codificada deve corresponder à senha esperada.");
    }
}
