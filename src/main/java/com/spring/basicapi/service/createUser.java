package com.spring.basicapi.service;

import com.spring.basicapi.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class createUser {
    @Autowired
    private UserRepo userRepo;



}
