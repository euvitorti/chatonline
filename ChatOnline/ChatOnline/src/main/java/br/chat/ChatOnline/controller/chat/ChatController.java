package br.chat.ChatOnline.controller.chat;

import br.chat.ChatOnline.models.chat.Message;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/users/chat")
public class ChatController {

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    @GetMapping
    public Message sendMessage(Message message) throws Exception {
        // Aqui você pode adicionar lógica adicional, se necessário
        return message; // A mensagem será enviada para todos os conectados
    }
}
