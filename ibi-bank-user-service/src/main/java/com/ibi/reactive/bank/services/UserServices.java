package com.ibi.reactive.bank.services;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.ibi.reactive.bank.entities.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public interface UserServices {

	Flux<User> getAllUser();
	public Mono<List<SimpleGrantedAuthority>> findAuthoritiesForUserName(String username);
	}
