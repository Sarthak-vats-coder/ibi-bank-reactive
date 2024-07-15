package com.ibi.reactive.bank.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.ibi.reactive.bank.entities.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String>{

	Mono<User> findByEmail(String email);
	Mono<User> findByUsername(String username);

}