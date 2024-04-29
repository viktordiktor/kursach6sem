package com.nikonenko.kursach6sem.config.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/recreation-objects/**").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/general/**").permitAll()
                        .requestMatchers( "/public/**").permitAll()
                        .requestMatchers("/api/v1/users/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/api/v1/auth/login")
                        .loginProcessingUrl("/api/v1/auth/process_login")
                        .defaultSuccessUrl("/api/v1/recreation-objects", true)
                        .failureUrl("/api/v1/auth/login?error")
                )
                .logout(logout -> logout
                        .logoutUrl("/api/v1/logout")
                        .logoutSuccessUrl("/api/v1/auth/login"));
        return http.build();
    }
}