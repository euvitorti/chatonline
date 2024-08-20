package br.chat.ChatOnline.infra.security;

import br.chat.ChatOnline.repository.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// O @Component é utilizado para que o Spring carregue uma classe/componente genérico

@Component
public class Filter extends OncePerRequestFilter {

    @Autowired
    private TokenJwt tokenJwtService;

    @Autowired
    private IUserRepository iUserRepository;

    // É IMPORTANTE DETERMINAR A ORDEM DOS FILTROS APLICADOS
    // O PADRÃO DO SPRING É CHAMAR O FILTRO DELE, VERIFICAR SE O USUÁRIO ESTÁ LOGADO

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        var tokenJWT = recoverToken(request);

        if (tokenJWT != null) {
            var subject = tokenJwtService.getSubject(tokenJWT);

            // AUTENTICANOD O USUÁRIO, POIS O SPRING NÃO SABE QUE O USUÁRIO ESTÁ LOGADO
            var user = iUserRepository.findByUsername(subject);
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // NECESSÁRIO PARA CHAMAR OS PRÓXIMOS FILTROS NA APLICAÇÃO
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {

        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer", "");
        }

        return null;
    }
}