package edu.ukma.blog.security;

import java.util.concurrent.TimeUnit;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = TimeUnit.HOURS.toMillis(2);
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String TOKEN_SECRET = "1234567890";
}
