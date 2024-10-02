package br.chat.ChatOnline.socket.server;

import br.chat.ChatOnline.infra.security.TokenJwt;
import br.chat.ChatOnline.models.user.User;
import br.chat.ChatOnline.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Enable WebSocket message handling
@Order(Ordered.HIGHEST_PRECEDENCE + 99) // Setting the order of this configuration
@RequiredArgsConstructor // Generates a constructor for dependency injection
@Slf4j // Lombok annotation for logging
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private TokenJwt jwtTokenUtil; // JWT utility for token handling

    @Autowired
    private AuthenticationService userDetailsService; // Service for user authentication

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registering STOMP endpoints for WebSocket connections
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://127.0.0.1:5501") // Allowing specific origins for CORS
                .setAllowedOrigins("http://127.0.0.1:5502") // Multiple origins can be set
                .withSockJS(); // Enabling SockJS fallback options
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Configuring the message broker for handling messages
        registry.enableSimpleBroker("/topic"); // Enabling a simple broker for subscriptions
        registry.setApplicationDestinationPrefixes("/app"); // Prefix for application routes
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // Configuring the inbound channel to intercept messages before processing
        registration.interceptors(new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                // Handling STOMP CONNECT command for user authentication
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    // Extracting the authorization header
                    String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
                    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                        String token = authorizationHeader.substring(7); // Extracting the token

                        try {
                            // Validating the token and setting the user in the security context
                            String username = jwtTokenUtil.getUsername(token);
                            User userDetails = userDetailsService.loadUserByUsername(username);
                            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(auth); // Setting authentication
                            accessor.setUser(auth); // Setting the user in the accessor
                        } catch (Exception e) {
                            // Handle exceptions (optional logging can be added here)
                        }
                    }
                }
                return message; // Returning the original message
            }
        });
    }
}
