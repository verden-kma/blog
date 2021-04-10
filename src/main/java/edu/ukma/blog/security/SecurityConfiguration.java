package edu.ukma.blog.security;

import edu.ukma.blog.services.interfaces.user_related.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final IJwtUtils jwtUtils;
    @Value("${refreshTokenUrl}")
    private final String refreshTokenUrl;
    private IUserService userDetailsService; // ex-final

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
                .anyRequest().authenticated()
                .and()
                .addFilter(new LoginFilter(jwtUtils, authenticationManagerBean()))
                .addFilterBefore(new JwtFilter(jwtUtils, userDetailsService), LoginFilter.class)
                .addFilterAfter(new RefreshJwtFilter(new AntPathRequestMatcher(refreshTokenUrl, HttpMethod.GET.name()), jwtUtils), LoginFilter.class)
// todo: add logout for jwt
//                .addFilterAfter(new LogoutFilter((request, response, authentication) ->
//                        response.setStatus(HttpStatus.NO_CONTENT.value()), new SecurityContextLogoutHandler()), RefreshJwtFilter.class)
                .exceptionHandling().authenticationEntryPoint((request, response, authException) ->
        {
            authException.printStackTrace();
            response.sendError(HttpStatus.UNAUTHORIZED.value(), authException.getMessage());
        })
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    // attention&kek: 'corsFilter()' works, 'corsFilterBean()' doesn't
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
