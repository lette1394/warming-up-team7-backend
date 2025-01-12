package com.warmup.familytalk.web;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static com.warmup.familytalk.chats.ChatRouter.CHAT_INFO_URL;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> swe.getResponse()
                                                                                 .setStatusCode(HttpStatus.UNAUTHORIZED)))
                .accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> swe.getResponse()
                                                                            .setStatusCode(HttpStatus.FORBIDDEN))).and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/l7_health_check").permitAll()
                .pathMatchers("/chat/**").permitAll()
                .pathMatchers("/rooms").authenticated()
                .pathMatchers("/rooms/**").authenticated()
                .pathMatchers(CHAT_INFO_URL).permitAll()
                .pathMatchers(CHAT_INFO_URL + "/**").permitAll()
                .pathMatchers("/test/**").permitAll()
                .pathMatchers("/login").permitAll()
                .pathMatchers("/register").permitAll()
                .anyExchange().authenticated()
                .and().build();
    }
}