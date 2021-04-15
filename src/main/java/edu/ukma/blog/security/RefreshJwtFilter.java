package edu.ukma.blog.security;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletResponse;
import java.util.function.BiConsumer;

public class RefreshJwtFilter extends ValidateAndDo {
    public RefreshJwtFilter(RequestMatcher requestMatcher, BiConsumer<HttpServletResponse, String> doUsernameAction) {
        super(requestMatcher, doUsernameAction);
    }
//    private final RequestMatcher requestMatcher;
//    private final IJwtUtils jwtUtils;
//    private final IBlacklistTokenService blacklistTokenService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
////        if (!requestMatcher.matches(request)) {
////            filterChain.doFilter(request, response);
////            return;
////        }
////
////        if (SecurityContextHolder.getContext().getAuthentication() == null) {
////            response.setStatus(HttpStatus.UNAUTHORIZED.value());
////            return;
////        }
////        Object userToken = SecurityContextHolder.getContext().getAuthentication();
////        String username = (String) ((UsernamePasswordAuthenticationToken) userToken).getPrincipal();
//        if (!blacklistTokenService.checkIsValid(username)) {
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            return;
//        }
//
//        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
//        response.getWriter().write(jwtUtils.generateJwt(username));
//    }
}
