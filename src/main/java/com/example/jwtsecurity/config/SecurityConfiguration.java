package com.example.jwtsecurity.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(httpSecurityCsrfConfigurer -> { httpSecurityCsrfConfigurer.disable(); })
                .cors(httpSecurityCorsConfigurer -> {  httpSecurityCorsConfigurer.disable(); })
                .sessionManagement(httpSecuritySessionManagementConfigurer -> {
                    httpSecuritySessionManagementConfigurer
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers( new AntPathRequestMatcher("/api/v1/auth/**")).permitAll()
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}

/**
 * http
 *             .csrf().disable()
 *             .authorizeHttpRequests()
 *             .requestMatchers("")
 *             .permitAll()
 *             .anyRequest()
 *             .authenticated()
 *             .and()
 *             .sessionManagement()
 *             .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
 *             .and()
 *             .authenticationProvider(authenticationProvider)
 *             .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
 */