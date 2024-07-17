package com.ibi.reactive.bank.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.NonNull;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	@Pattern(regexp = "[\\w]{3,20}")
	@Indexed(unique = true)
	@NonNull
	private String username;
	@Indexed(unique = true)
	@NonNull
	private String email;
	@NonNull
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	@NonNull
	private String fname;
	@NonNull
	private String lname;
	@NonNull
	private Integer age;
	
	private List<SimpleGrantedAuthority> authorities;
	

}
