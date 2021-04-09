package edu.ukma.blog.security;

public interface IJwtUtils {
    String generateJwt(String username);

    String extractUsername(String jwt);

    boolean validateJwt(String token);
}
