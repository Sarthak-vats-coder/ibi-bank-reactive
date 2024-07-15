package com.ibi.reactive.bank.config;

import java.security.Security;
import java.util.List;

import org.springframework.http.HttpCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.ibi.reactive.bank.entities.User;
import com.ibi.reactive.bank.repositories.UserRepository;
import com.ibi.reactive.bank.services.UserServices;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
@Component
@Slf4j
public class JwtTokenValidator implements WebFilter {
	
	
	JwtTokenProvider jwtTokenProvider;
	UserServices userServices;
	UserRepository userRepository;
	public JwtTokenValidator(JwtTokenProvider jwtTokenProvider,	UserServices userServices,UserRepository userRepository) {
		this.jwtTokenProvider  = jwtTokenProvider;
		this.userRepository =userRepository;
		this.userServices=userServices;
	}
	
	
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
			
			if(exchange.getRequest().getCookies()==null) {
				log.atInfo().log("authenticating............");
				return chain.filter(exchange);
			}
			String jwt = exchange.getRequest().getCookies().getFirst("auth_token").getValue();
			String username = JwtTokenProvider.getUsernameFromJwToken(jwt);

			Mono<User> userToAuthMono = userRepository.findByUsername(username);
		return userToAuthMono.flatMap(user -> {
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword(),user.getAuthorities());
			SecurityContext securityContext = new SecurityContextImpl(authentication);
			
			return chain.filter(exchange)
					.contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
		});
	}
	
	
}
