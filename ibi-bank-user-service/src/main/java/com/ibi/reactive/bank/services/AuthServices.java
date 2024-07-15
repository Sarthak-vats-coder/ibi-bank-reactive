package com.ibi.reactive.bank.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import com.ibi.reactive.bank.entities.User;
import com.ibi.reactive.bank.models.SignInRequest;

import reactor.core.publisher.Mono;

public interface AuthServices {
	
	Mono<User> register(User user) throws Exception;

	Mono<ResponseEntity<String>> signIn(SignInRequest request,ServerWebExchange exchange) ;

	Mono<Authentication> createAuthenticationForUsernamePassword(String username, String password);
}
