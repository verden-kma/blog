package edu.ukma.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ukma.blog.models.user.requests.UserLoginRequest;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final IJwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public LoginFilter(IJwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    // fixme: maybe add permissions here
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserLoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), UserLoginRequest.class);

        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        return authenticationManager.authenticate(userToken);
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                         FilterChain chain, Authentication auth) throws IOException {
        SecurityContextHolder.getContext().setAuthentication(auth);
        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        response.getWriter().write(jwtUtils.generateJwt(username));
    }

    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                           AuthenticationException failed) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
