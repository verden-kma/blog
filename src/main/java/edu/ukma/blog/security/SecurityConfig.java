package edu.ukma.blog.security;

import edu.ukma.blog.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${signUpUrl}")
    private final String SIGN_UP_URL;

    @Value("${confirmSignUpUrl}")
    private final String CONFIRM_SIGNUP_TEMPLATE;

    @Value("${expirationTime}")
    private final long EXPIRATION_TIME;

    @Value("${tokenSecret}")
    private final String TOKEN_SECRET;

    @Value("${tokenPrefix}")
    private final String TOKEN_PREFIX;

    private final IUserService userDetailService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL, CONFIRM_SIGNUP_TEMPLATE + "**")
                .permitAll()

                .antMatchers("/h2-console/**").permitAll()

                .anyRequest()
                .authenticated()
                .and()
                .addFilter(getAuthFilter())
                .addFilter(new JWTValidationFilter(TOKEN_PREFIX, TOKEN_SECRET, authenticationManager()))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.cors();
        httpSecurity.headers().frameOptions().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authMng) throws Exception {
        authMng.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
    }

    private AuthenticationFilter getAuthFilter() throws Exception {
        AuthenticationFilter authFilter = new AuthenticationFilter(EXPIRATION_TIME,
                TOKEN_SECRET, TOKEN_PREFIX, authenticationManager());
        authFilter.setFilterProcessesUrl("/users/login");
        return authFilter;
    }
}
