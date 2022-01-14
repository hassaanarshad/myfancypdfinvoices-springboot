package com.practice.myfancypdfinvoices.springboot.service;

import java.util.UUID;

import com.practice.myfancypdfinvoices.springboot.model.User;

import org.springframework.stereotype.Component;

@Component
public class UserService {
    
    public User findById(String id) {
        String randomName = UUID.randomUUID().toString();

        return new User(id, randomName);
    }
}
