package br.chat.ChatOnline.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class Security  implements WebMvcConfigurer{

    @Autowired
    private Filter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // Para desabilitar a proteção ataques do tipo CSRF (Cross-Site Request Forgery)
        // Motivo: O TOKEN já vem habilitado para esse tipo de ataque, logo seria redundante

        return httpSecurity.csrf(c -> c.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // A REQUISIÇÃO É LIBERADA
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/login.html").permitAll();
                    req.requestMatchers("/users").permitAll();
                    req.requestMatchers("/login").permitAll();
                    req.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "swagger-ui/**").permitAll();
                    // QUALQUER OUTRA REQUISIÇÃO ESTÁ BLOQUEADA
                    req.anyRequest().authenticated();
                })
                // ESTE FILTRO IRÁ VIM ANTES DO FILTRO DO SPRING
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // O @BEAN SERVE PARA EXPORTAR UMA CLASSE PARA O SPRING,
    // FAZENDO COM QUE ELE CONSIGA CARREGÁ-LA E REALIZE A SUA INJEÇÃO DE DEPENDÊNCIA EM OUTRAS CLASSES

    // CRIAR UM OBJETO AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins("ADICIONE AQUI A URL PERMITIDA")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");
    }
}