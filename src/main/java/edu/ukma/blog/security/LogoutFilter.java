package edu.ukma.blog.security;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletResponse;
import java.util.function.BiConsumer;

public class LogoutFilter extends ValidateAndDo {
    public LogoutFilter(RequestMatcher requestMatcher, BiConsumer<HttpServletResponse, String> doUsernameAction) {
        super(requestMatcher, doUsernameAction);
    }
}
