package edu.ukma.blog.security;

import edu.ukma.blog.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String SIGNUP_ENDPOINT = "/users";
    private final IUserService userDetailService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGNUP_ENDPOINT)
                .permitAll()
                .anyRequest()
                .authenticated();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authMng) throws Exception {
        authMng.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
    }
}
