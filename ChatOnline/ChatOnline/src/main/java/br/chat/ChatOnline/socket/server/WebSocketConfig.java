package br.chat.ChatOnline.socket.server;

import br.chat.ChatOnline.models.ChatMessage;
import br.chat.ChatOnline.models.OutPutMessage;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat");
    }


    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutPutMessage send(ChatMessage message) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutPutMessage(message.getFrom(), message.getText());
    }
}


Aqui estão algumas sugestões para ajustar o código para que funcione sem o SockJS, mantendo a configuração de WebSocket pura:

Remover SockJS:

Já que você não quer usar SockJS, remova o método withSockJS() na configuração de endpoints. Isso deixará apenas o WebSocket puro.
java
Copiar código
@Override
public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/chat");
}
Separar Configuração WebSocket e Lógica de Mensagens:

Considere separar a configuração do WebSocket da lógica de processamento de mensagens, criando uma classe para cada responsabilidade.
Adicionar Controle de Erros:

Como está configurando WebSocket puro, adicione controle de erros e validações, especialmente se estiver lidando com múltiplos tipos de mensagens ou clientes.
Aprimorar a Classe OutPutMessage:

Adicione um campo de data na classe OutPutMessage para armazenar o timestamp da mensagem.
java
Copiar código
private String time;

public OutPutMessage(String from, String text, String time) {
    this.from = from;
    this.text = text;
    this.time = time;
}

public String getTime() {
    return time;
}

public void setTime(String time) {
    this.time = time;
}
Atualize a lógica em send para incluir o timestamp.
        java
Copiar código
return new OutPutMessage(message.getFrom(), message.getText(), time);
Validar Mensagens Recebidas:

Antes de enviar a mensagem, valide os campos em ChatMessage para evitar mensagens malformadas ou incompletas.
Remover Dependências Não Utilizadas:

Verifique se há importações ou dependências que não estão sendo usadas e remova-as para manter o código limpo.
Essas mudanças vão simplificar seu código e remover o uso do SockJS, enquanto ainda permite o uso de WebSocket puro.






