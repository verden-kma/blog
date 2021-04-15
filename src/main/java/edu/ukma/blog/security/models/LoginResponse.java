package edu.ukma.blog.security.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class LoginResponse {
    private final String token;
    private final long expiration;
    private final List<String> authorities;
}