//package br.chat.ChatOnline.socket.server;
//
//import br.chat.ChatOnline.infra.security.TokenJwt;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.HandshakeInterceptor;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//public class JwtHandshakeInterceptor implements HandshakeInterceptor {
//
//    private final TokenJwt jwtTokenProvider;
//
//    public JwtHandshakeInterceptor(TokenJwt jwtTokenProvider) {
//        this.jwtTokenProvider = jwtTokenProvider;
//    }
//
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
//                                   Map<String, Object> attributes) throws Exception {
//
//        String token = extractToken(request);
//
//        if (token != null) {
//            try {
//                String username = jwtTokenProvider.getSubject(token);
//                attributes.put("user", username); // Adiciona o nome de usuário aos atributos
//                return true;
//            } catch (RuntimeException e) {
//                // Token inválido ou expirado, a conexão não é permitida
//                return false;
//            }
//        }
//
//        return false; // Conexão não é permitida se o token não for encontrado ou for inválido
//    }
//
//    @Override
//    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
//                               Exception exception) {
//        // Implementar se necessário
//    }
//
//    private String extractToken(ServerHttpRequest request) {
//        // Tenta extrair o token do header Authorization (Bearer token)
//        List<String> authorizationHeaders = request.getHeaders().get("Authorization");
//        if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
//            String bearerToken = authorizationHeaders.get(0);
//            if (bearerToken.startsWith("Bearer ")) {
//                return bearerToken.substring(7); // Retorna o token sem o prefixo "Bearer "
//            }
//        }
//
//        // Caso o token não esteja no header Authorization, você pode tentar extrair de query parameters
//        return Optional.ofNullable(request.getURI().getQuery())
//                .flatMap(query -> {
//                    // Busca um token em "token=SEU_TOKEN"
//                    for (String param : query.split("&")) {
//                        String[] pair = param.split("=");
//                        if (pair.length > 1 && "token".equals(pair[0])) {
//                            return Optional.of(pair[1]);
//                        }
//                    }
//                    return Optional.empty();
//                })
//                .orElse(null); // Retorna null se não encontrar o token
//    }
//}
