package br.chat.ChatOnline.infra.security;

import br.chat.ChatOnline.models.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.function.Function;

// Service annotation indicates that this class provides token-related functionality
@Service
public class TokenJwt {

    // Secret key used for signing JWT tokens.
    // It is recommended to store this value in a properties file
    // to enhance security and allow for easy configuration changes.
    private String secret = "dontgiveup";


    // Method to generate a JWT token for a given user
    public String generateToken(User user) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API Chat") // Sets the issuer of the token
                    .withSubject(user.getUsername()) // Sets the subject to the username
                    .withExpiresAt(expirationDate()) // Sets the expiration date
                    .sign(algorithm); // Signs the token with the specified algorithm
        } catch (JWTCreationException exception) {
            // Handle token creation errors
            throw new RuntimeException("Error generating the token.");
        }
    }

    // Method to calculate the expiration date for the token (2 hours from now)
    private Instant expirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    // Method to retrieve the subject (username) from the JWT token
    public String getSubject(String tokenJWT) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("API Chat") // Specify the expected issuer
                    .build()
                    .verify(tokenJWT) // Verify the token
                    .getSubject(); // Return the subject of the token
        } catch (JWTVerificationException exception) {
            // Handle invalid token exceptions
            throw new RuntimeException("Invalid or expired token!");
        }
    }

    // Generic method to get a specific claim from the token
    public <T> T getClaim(String token, Function<Map<String, Claim>, T> claimsResolver) {
        Map<String, Claim> claims = getAllClaims(token);
        return claimsResolver.apply(claims); // Apply the provided function to resolve the claims
    }

    // Method to get the username from the JWT token
    public String getUsername(String token) {
        return getClaim(token, claims -> claims.get("sub").asString()); // Resolve the "sub" claim
    }

    // Method to get all claims from the token
    public Map<String, Claim> getAllClaims(String token) {
        var algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("API Chat") // Specify the expected issuer
                .build()
                .verify(token) // Verify the token
                .getClaims(); // Retrieve and return all claims
    }
}
