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

// Configuration class for Spring Security
@Configuration
@EnableWebSecurity
public class Security implements WebMvcConfigurer {

    @Autowired
    private Filter filter; // Custom JWT filter for authentication

    // SecurityFilterChain bean to define security configurations
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(c -> c.disable()) // Disable CSRF protection for stateless APIs
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configure session management to be stateless
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/login.html", "/signin.html").permitAll(); // Allow public access to login pages
                    req.requestMatchers("/auth/login").permitAll(); // Permit login endpoint without authentication
                    req.requestMatchers("/users", "/ws/**").permitAll(); // Allow other public routes
                    req.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "swagger-ui/**").permitAll(); // Allow access to Swagger documentation
                    req.anyRequest().authenticated(); // Require authentication for all other requests
                })
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class) // Add the custom JWT filter before the default authentication filter
                .build(); // Build the security filter chain
    }

    // AuthenticationManager bean for managing authentication processes
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // PasswordEncoder bean to encrypt passwords using BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use BCrypt for password hashing
    }

    // CORS configuration to allow cross-origin requests
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**") // Allow CORS for all endpoints
                .allowedOrigins("http://127.0.0.1:5501", "http://127.0.0.1:5502") // Specify allowed origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Specify allowed HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow credentials to be included in CORS requests
    }
}
