package edu.ukma.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ukma.blog.SpringApplicationContext;
import edu.ukma.blog.models.user.RequestUserLogin;
import edu.ukma.blog.models.user.UserEntity;
import edu.ukma.blog.services.IUserService;
import edu.ukma.blog.services.implementations.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final String USER_SERVICE_BEAN_NAME;

    private final AuthenticationManager authMng;

    static {
        String implClassName = UserService.class.getSimpleName();
        USER_SERVICE_BEAN_NAME = implClassName.substring(0, 1).toLowerCase() + implClassName.substring(1);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            RequestUserLogin loginData = new ObjectMapper().readValue(request.getInputStream(), RequestUserLogin.class);
            return authMng.authenticate(new UsernamePasswordAuthenticationToken(
                    loginData.getUsername(),
                    loginData.getPassword(),
                    Collections.emptyList()
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) {
        String username = ((User) auth.getPrincipal()).getUsername();
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
                .compact();

        IUserService userService = (IUserService) SpringApplicationContext.getBean(USER_SERVICE_BEAN_NAME);
        UserEntity user = userService.getUserEntity(username);
        response.addHeader(SecurityConstants.AUTH_HEADER, SecurityConstants.TOKEN_PREFIX + token);
        response.addHeader("UserID", user.getPublicId());
    }

}
