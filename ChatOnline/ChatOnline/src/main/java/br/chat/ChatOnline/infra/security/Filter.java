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

// @Component annotation allows Spring to manage this class as a Spring bean
@Component
public class Filter extends OncePerRequestFilter {

    @Autowired
    private TokenJwt tokenJwtService; // Service for handling JWT operations

    @Autowired
    private IUserRepository iUserRepository; // Repository for user data

    // IMPORTANT: ORDER OF FILTERS MATTERS
    // Spring's default behavior is to call its own filter to check if the user is logged in

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        var tokenJWT = recoverToken(request); // Extract token from the request

        if (tokenJWT != null) {
            var subject = tokenJwtService.getSubject(tokenJWT); // Get username from the token

            // AUTHENTICATING THE USER, AS SPRING DOES NOT KNOW THAT THE USER IS LOGGED IN
            var user = iUserRepository.findByUsername(subject); // Retrieve user details from the repository
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()); // Create an authentication object

            SecurityContextHolder.getContext().setAuthentication(authentication); // Set the authentication in the Security context
        }

        // IMPORTANT: CONTINUE WITH THE NEXT FILTERS IN THE APPLICATION
        filterChain.doFilter(request, response); // Proceed to the next filter in the chain
    }

    // Method to extract the JWT from the Authorization header
    private String recoverToken(HttpServletRequest request) {

        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", ""); // Remove "Bearer " prefix from the token
        }

        return null; // Return null if no token is found
    }
}
