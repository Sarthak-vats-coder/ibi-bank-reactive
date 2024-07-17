package com.ibi.reactive.bank.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.ibi.reactive.bank.models.JwtTokenConstants;
import com.ibi.reactive.bank.models.SignInRequest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
@Service
public class JwtTokenProvider {

	static SecretKey key = Keys.hmacShaKeyFor(JwtTokenConstants.SECRET_KEY.getBytes());

	public String generateToken(SignInRequest signInRequest) {
		return  Jwts
				.builder()
				.setSubject(signInRequest.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + 1000 * 3600 * 24))
				.claim("username", signInRequest.getUsername())
				.claim("password", signInRequest.getPassword())
				.signWith(key).compact();
	}

	public static String getUsernameFromJwToken(String jwt) {
		return Jwts
				.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(jwt)
				.getBody()
				.get("username")
				.toString();
	}

	public static String getPasswordFromJwToken(String jwt) {
		return Jwts
				.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(jwt)
				.getBody()
				.get("password")
				.toString();

	}

}
