package br.chat.ChatOnline.controller.chat;

import br.chat.ChatOnline.dto.chat.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    @MessageMapping("/chat") // Endpoint for handling incoming chat messages
    @SendTo("/topic/messages") // Broadcasts the message to subscribed clients
    public MessageDTO chat(@Payload MessageDTO message) {
        try {
            return message; // Return the received message
        } catch (Exception e) {
            return null; // Returns null in case of an error, but this can be customized
        }
    }

}
