package dev.bti.achieverfarm.config;

import dev.bti.achieverfarm.security.filters.JwtRequestFilter;
import dev.bti.achieverfarm.security.filters.RouteExistenceFilter;
import dev.bti.achieverfarm.security.filters.UserValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserValidationFilter userValidationFilter;

    @Autowired
    private RouteExistenceFilter routeExistenceFilter;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auths -> auths
                        .requestMatchers("/api/v1/auth/**", "/terminal/**", "/api/terminal/**").permitAll()
                        .requestMatchers("/api/v1/user/**", "/api/v1/cart/**", "/api/v1/item/**",
                                "/api/v1/items/**", "/api/v1/order/**", "/api/v1/stock/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().denyAll())
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(routeExistenceFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtRequestFilter, RouteExistenceFilter.class);
        http.addFilterBefore(userValidationFilter, JwtRequestFilter.class);

        return http.build();
    }
}


