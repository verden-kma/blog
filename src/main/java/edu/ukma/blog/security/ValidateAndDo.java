package edu.ukma.blog.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class ValidateAndDo extends OncePerRequestFilter {
    private final RequestMatcher requestMatcher;
    private final BiConsumer<HttpServletResponse, String> doUsernameAction;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        Object userToken = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) ((UsernamePasswordAuthenticationToken) userToken).getPrincipal();
        doUsernameAction.accept(response, username);
    }
}
