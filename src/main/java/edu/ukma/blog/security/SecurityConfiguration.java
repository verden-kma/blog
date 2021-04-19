package edu.ukma.blog.security;

import edu.ukma.blog.security.services.IBlacklistTokenService;
import edu.ukma.blog.services.interfaces.user_related.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.BiConsumer;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final IJwtUtils jwtUtils;
    @Value("${refresh-token-url}")
    private final String refreshTokenUrl;
    @Value("${expiration-time}")
    private final long tokenExpiration;
    private IUserService userDetailsService; // ex-final
    private final IBlacklistTokenService blacklistTokenService;

    @Autowired
    public void setUserDetailsService(final IUserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder managerBuilder) throws Exception {
        managerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoderBean());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/users", "/users/confirm/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new LoginFilter(jwtUtils, authenticationManagerBean(), tokenExpiration, blacklistTokenService))
                .addFilterBefore(new JwtFilter(jwtUtils, userDetailsService, blacklistTokenService), LoginFilter.class)
                .addFilterAfter(new RefreshJwtFilter(new AntPathRequestMatcher(refreshTokenUrl, HttpMethod.GET.name()),
                        refreshJwtFilter()), LoginFilter.class)
                .addFilterAfter(new LogoutFilter(new AntPathRequestMatcher("/logout-invalidate", HttpMethod.POST.name()),
                        logoutFilter()), RefreshJwtFilter.class)
                .exceptionHandling().authenticationEntryPoint((request, response, authException) ->
        {
            authException.printStackTrace();
            response.sendError(HttpStatus.UNAUTHORIZED.value(), authException.getMessage());
        })
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;
        http.headers().frameOptions().sameOrigin();
    }


    private BiConsumer<HttpServletResponse, String> refreshJwtFilter() {
        return (response, username) -> {
            if (!blacklistTokenService.checkIsValid(username)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
            try {
                response.getWriter().write(jwtUtils.generateJwt(username));
            } catch (IOException e) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        };
    }

    private BiConsumer<HttpServletResponse, String> logoutFilter() {
        return (response, username) -> {
            blacklistTokenService.setIsInvalid(username);
            response.setStatus(HttpStatus.NO_CONTENT.value());
        };
    }

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedOrigin("*");
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("OPTIONS");
        corsConfig.addAllowedMethod("HEAD");
        corsConfig.addAllowedMethod("GET");
        corsConfig.addAllowedMethod("PUT");
        corsConfig.addAllowedMethod("POST");
        corsConfig.addAllowedMethod("PATCH");
        corsConfig.addAllowedMethod("DELETE");
        configurationSource.registerCorsConfiguration("/**", corsConfig);
        return new CorsFilter(configurationSource);
    }


}
