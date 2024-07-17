package com.ibi.reactive.bank.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;

import com.ibi.reactive.bank.repositories.UserRepository;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {
	
	JwtTokenValidator jwtTokenValidator;
	UserRepository userRepository;
	
	public  WebSecurityConfig(JwtTokenValidator jwtTokenValidator,UserRepository userRepository) {
		this.jwtTokenValidator= jwtTokenValidator;
		this.userRepository = userRepository;
	}
	
	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Primary
	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
		return httpSecurity.csrf(CsrfSpec::disable)
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.httpBasic(http -> http.authenticationEntryPoint(new NoPopupAuthenticationEntryPoint()))
				.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
				.authorizeExchange(authorize -> authorize.pathMatchers("/auth/register").permitAll()
						.pathMatchers("/auth/signIn").permitAll()
						.anyExchange().authenticated())
				.addFilterBefore(jwtTokenValidator ,SecurityWebFiltersOrder.AUTHENTICATION)
				.formLogin(FormLoginSpec::disable)
				.build();
	}

	private CorsConfigurationSource corsConfigurationSource() {
		return new CorsConfigurationSource() {
			@Override
			public CorsConfiguration getCorsConfiguration(ServerWebExchange exchange) {
				CorsConfiguration ccfg = new CorsConfiguration();
				ccfg.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://127.0.0.1:3000",
						"http://localhost:1025", "http://127.0.0.1:1025"));
				ccfg.setAllowedMethods(Collections.singletonList("*"));
				ccfg.setAllowCredentials(true);
				ccfg.setAllowedHeaders(Collections.singletonList("*"));
				ccfg.setMaxAge(3600L);

				return ccfg;
			}
		};

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
}
