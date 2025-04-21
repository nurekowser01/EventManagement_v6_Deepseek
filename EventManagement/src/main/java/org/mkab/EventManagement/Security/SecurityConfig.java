package org.mkab.EventManagement.Security;

import org.mkab.EventManagement.auth.JwtFilter;

// Step 4: Spring Security Configuration

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

 @Autowired
 private JwtFilter jwtFilter;

 @Autowired
 private CustomUserDetailsService userDetailsService;

 @Bean
 public PasswordEncoder passwordEncoder() {
     return new BCryptPasswordEncoder();
 }
 @Bean
 public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
     return config.getAuthenticationManager();
 }

 @Bean
 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     http.csrf().disable()
         .authorizeHttpRequests(auth -> auth
             .requestMatchers("/api/auth/**").permitAll()
             .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**").permitAll()
             .anyRequest().authenticated()
         )
         .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

     http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

     return http.build();
 }

}