package br.chat.ChatOnline.controller;

import br.chat.ChatOnline.models.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) throws Exception {
        // Aqui você pode adicionar lógica adicional, se necessário
        return message; // A mensagem será enviada para todos os conectados
    }
}
