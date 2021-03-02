package edu.ukma.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ukma.blog.models.user.requests.UserLoginRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
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
    private final long EXPIRATION_TIME;
    private final String TOKEN_SECRET;
    private final String TOKEN_PREFIX;

    private final AuthenticationManager authMng;
    private final ObjectMapper jacksonObjectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            UserLoginRequest loginData = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequest.class);
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
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET)
                .compact();

        @Data
        class AuthResp {
            final String authType = TOKEN_PREFIX;
            String token;

            public AuthResp(String token) {
                this.token = token;
            }
        }

        try {
            response.setStatus(HttpServletResponse.SC_OK);
            jacksonObjectMapper.writeValue(response.getWriter(), new AuthResp(token));
            response.getWriter().close();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
