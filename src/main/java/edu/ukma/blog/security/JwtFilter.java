package edu.ukma.blog.security;

import edu.ukma.blog.security.services.IBlacklistTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final IJwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final IBlacklistTokenService blacklistTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.split(" ")[1];
        String username = jwtUtils.extractUsername(jwt);
        if (!jwtUtils.validateJwt(jwt) || !blacklistTokenService.checkIsValid(username)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(userToken);
        filterChain.doFilter(request, response);
    }
}
