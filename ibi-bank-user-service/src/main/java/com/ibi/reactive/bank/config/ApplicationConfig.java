package com.ibi.reactive.bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ibi.reactive.bank.repositories.UserRepository;

@Configuration
public class ApplicationConfig {
	
	UserRepository userRepository;
	public ApplicationConfig(UserRepository userRepository) {
		this .userRepository = userRepository;
	}
	
	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
//	@Bean
//    public ReactiveUserDetailsService userDetailsService() {
//        return username -> userRepository.findByUsername(username)
//                .map(user -> org.springframework.security.core.userdetails.User
//                        .withUsername(user.getUsername())
//                        .password(user.getPassword())
//                        .authorities(user.getAuthorities()) 
//                        .build())
//                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")));
//    }
//	
//	@Bean
//    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
//        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
//        authenticationManager.setPasswordEncoder(passwordEncoder);
//        return authenticationManager;
//    }
//	

}
