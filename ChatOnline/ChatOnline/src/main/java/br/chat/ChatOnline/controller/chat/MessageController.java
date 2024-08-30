package br.chat.ChatOnline.controller.chat;

import br.chat.ChatOnline.dto.chat.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * Manipula mensagens recebidas no destino /chat e as envia para o usuário específico.
     *
     * @param message A mensagem recebida, contendo o nome de usuário e o conteúdo da mensagem.
     */
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public MessageDTO chat(@Payload MessageDTO message) {
        try {
            // O log é opcional, mas pode ajudar a depurar ou monitorar as mensagens
            return message;
        } catch (Exception e) {
            return null; // Retorna null em caso de erro, mas isso pode ser personalizado
        }
    }

}
