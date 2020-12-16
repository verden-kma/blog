package edu.ukma.blog.security;

import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;


public class JWTValidationFilter extends BasicAuthenticationFilter {
    public JWTValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            UsernamePasswordAuthenticationToken authToken = getAuthToken(authHeader);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthToken(String authHeader) {
        String token = authHeader.substring(SecurityConstants.TOKEN_PREFIX.length());
        String user = Jwts.parser()
                .setSigningKey(SecurityConstants.TOKEN_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        if (user == null) return null;
        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }

}
