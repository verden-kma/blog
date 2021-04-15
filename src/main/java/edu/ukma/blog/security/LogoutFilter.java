package edu.ukma.blog.security;


import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletResponse;
import java.util.function.BiConsumer;

public class LogoutFilter extends ValidateAndDo {
    public LogoutFilter(RequestMatcher requestMatcher, BiConsumer<HttpServletResponse, String> doUsernameAction) {
        super(requestMatcher, doUsernameAction);
    }
//    private final RequestMatcher requestMatcher;
//    private final IBlacklistTokenService blacklistTokenService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
////        if (!requestMatcher.matches(request)) {
////            filterChain.doFilter(request, response);
////            return;
////        }
////
////        if (SecurityContextHolder.getContext().getAuthentication() == null) {
////            response.setStatus(HttpStatus.UNAUTHORIZED.value());
////            return;
////        }
////
////        Object userToken = SecurityContextHolder.getContext().getAuthentication();
////        String username = (String) ((UsernamePasswordAuthenticationToken) userToken).getPrincipal();
//        blacklistTokenService.setIsInvalid(username);
//
//        response.setStatus(HttpStatus.NO_CONTENT.value());
//    }
}
