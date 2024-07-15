package com.ibi.reactive.bank.serviceImpl;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.ibi.reactive.bank.entities.User;
import com.ibi.reactive.bank.repositories.UserRepository;
import com.ibi.reactive.bank.services.UserServices;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class UserServiceImpl implements UserServices{
	
	UserRepository userRepository;
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Flux<User> getAllUser() {
		return userRepository.findAll();
	}

	@Override
	public Mono<List<SimpleGrantedAuthority>> findAuthoritiesForUserName(String username) {
		
		return userRepository
				.findByUsername(username)
				.map(user -> user.getAuthorities());
	}

}

	

	
