package com.ibi.reactive.bank.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {
	
	JwtTokenValidator jwtTokenValidator;
	
	public  WebSecurityConfig(JwtTokenValidator jwtTokenValidator) {
		this.jwtTokenValidator= jwtTokenValidator;
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
}
