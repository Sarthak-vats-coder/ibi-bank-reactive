package com.ibi.reactive.bank.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import com.ibi.reactive.bank.entities.User;
import com.ibi.reactive.bank.models.SignInRequest;
import com.ibi.reactive.bank.services.AuthServices;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;
@RestControllerAdvice
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	AuthServices authServices;
	
	public AuthController(AuthServices authServices) {
		this.authServices = authServices;
		
	}
	
	@PostMapping("/register")
	public Mono<User> register(@RequestBody User user) throws Exception{
		return authServices.register(user);
		}
	
	@PostMapping("/signIn")
	public ResponseEntity<ResponseEntity<String>> signIn( @RequestBody SignInRequest request,ServerWebExchange exchange) {
		return ResponseEntity.ok(authServices.signIn(request, exchange)) ;
		}
	
	

}
