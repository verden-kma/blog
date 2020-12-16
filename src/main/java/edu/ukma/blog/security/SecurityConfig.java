package edu.ukma.blog.security;

import edu.ukma.blog.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static edu.ukma.blog.security.SecurityConstants.SIGN_UP_URL;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final IUserService userDetailService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(getAuthFilter())
                .addFilter(new JWTValidationFilter(authenticationManager()))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.cors();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authMng) throws Exception {
        authMng.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
    }

    private AuthenticationFilter getAuthFilter() throws Exception {
        AuthenticationFilter authFilter = new AuthenticationFilter(authenticationManager());
        authFilter.setFilterProcessesUrl("/users/login");
        return authFilter;
    }
}
