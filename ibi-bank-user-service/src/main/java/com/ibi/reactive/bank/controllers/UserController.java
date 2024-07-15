package com.ibi.reactive.bank.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ibi.reactive.bank.entities.User;
import com.ibi.reactive.bank.repositories.UserRepository;
import com.ibi.reactive.bank.services.UserServices;

import reactor.core.publisher.Flux;
@RestControllerAdvice
@RestController
@RequestMapping("/user")
public class UserController {
    
    UserRepository userRepository;
    UserServices userServices;

    public UserController(UserRepository userRepository,UserServices userServices) {
        this.userRepository = userRepository;
        this.userServices = userServices;
    }
    
//    @GetMapping("/insertDummyRecord")
//    public Mono<User> saveDummyRecord() {
//        return userServices.saveUser(User.builder()
//                        .email("sarthak@ibi.com")
//                        .username("sarthak")
//                        .build())
//                .doOnError(Throwable::printStackTrace);
//    }
    
    @GetMapping("/all")
    public Flux<User> getAllUsers(){
        return userServices.getAllUser();
    }
}