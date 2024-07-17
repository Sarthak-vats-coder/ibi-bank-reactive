package com.ibi.reactive.bank.serviceImpl;

import java.util.ArrayList;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import com.ibi.reactive.bank.config.JwtTokenProvider;
import com.ibi.reactive.bank.entities.User;
import com.ibi.reactive.bank.models.SignInRequest;
import com.ibi.reactive.bank.repositories.UserRepository;
import com.ibi.reactive.bank.services.AuthServices;
import com.ibi.reactive.bank.services.UserServices;

import io.netty.handler.codec.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
@Service
@Slf4j
public class AuthServiceImpl implements AuthServices{

	UserServices userServices;
	UserRepository userRepository;
	PasswordEncoder passwordEncoder;
	JwtTokenProvider jwtTokenProvider;
	public AuthServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder,UserServices userServices,JwtTokenProvider jwtTokenProvider) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userServices = userServices;
		this.jwtTokenProvider = jwtTokenProvider;
	}
	
	@Override
	public Mono<User> register(User user) throws Exception {
		log.atInfo().log("User: {}", user);
		return userRepository
				.findByUsername(user.getUsername())
				.doOnNext(existingUser -> new Exception("User already exists: " + existingUser.getUsername()))
				.switchIfEmpty(userRepository
						.save(User.builder()
							.email(user.getEmail())
							.username(user.getUsername())
							.password(passwordEncoder.encode(user.getPassword()))
							.age(user.getAge())
							.fname(user.getFname())
							.lname(user.getLname())
							.authorities(new ArrayList<>())
							.build()));
	}

	@Override
	public ResponseEntity<String> signIn(SignInRequest request, ServerWebExchange exchange) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtTokenProvider.generateToken(request);
		exchange.getResponse().addCookie(ResponseCookie
                .from("auth_token", jwt)
                .path("/")
                .httpOnly(false)
                .domain("localhost")
                .secure(false)
                .maxAge(24 * 3600)
                .build());
		
		
//	    return createAuthenticationForUsername(request.getUsername())
//	            .map(auth -> {
//	            	log.atInfo().log("Generating and appending cookie");
//	                
//	                exchange.getResponse().addCookie(ResponseCookie
//	                        .from("auth_token", jwt)
//	                        .path("/")
//	                        .httpOnly(false)
//	                        .domain("localhost")
//	                        .secure(false)
//	                        .maxAge(24 * 3600)
//	                        .build());
	               
	                return ResponseEntity.ok("Signed in successfully");
//	            });
	            
	}
		
	
	
	
	@Override
	public Mono<Authentication> createAuthenticationForUsername(String username){
		return userRepository
			.findByUsername(username)
			.zipWith(
					userServices.findAuthoritiesForUserName(username),
					(user, authorities) -> new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), authorities)
					);
	}

}
