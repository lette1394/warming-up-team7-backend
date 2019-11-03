package com.warmup.familytalk.web;

import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import com.warmup.familytalk.register.model.JwtToken;
import com.warmup.familytalk.register.service.JwtService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtService jwtService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        final String authToken = authentication.getCredentials().toString();

        return jwtService.parse(authToken)
                         .publishOn(Schedulers.elastic())
                         .filter(JwtToken::isValid)
                         .map(this::toUsernamePasswordAuthenticationToken);
    }

    private UsernamePasswordAuthenticationToken toUsernamePasswordAuthenticationToken(JwtToken jwtToken) {
        return new UsernamePasswordAuthenticationToken(
                jwtToken.getUserId(),
                null, // unused
                jwtToken.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .collect(Collectors.toList())
        );
    }
}