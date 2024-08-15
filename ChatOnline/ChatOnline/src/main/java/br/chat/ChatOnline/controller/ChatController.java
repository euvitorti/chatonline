package br.chat.ChatOnline.controller;

import br.chat.ChatOnline.models.ChatMessage;
import br.chat.ChatOnline.models.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class ChatController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public ChatMessage chatMessage(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new ChatMessage("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
}