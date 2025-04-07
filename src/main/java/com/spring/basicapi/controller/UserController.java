package com.spring.basicapi.controller;


import com.spring.basicapi.dto.LoginResponse;
import com.spring.basicapi.dto.UserDTO;
import com.spring.basicapi.model.User;
import com.spring.basicapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public User createUser(@RequestBody UserDTO userDTO){
        return Optional.ofNullable(userService.createUser(userDTO))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ERRO WHEN CREATE USER!"));
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody UserDTO userDTO) {
        return userService.loginUser(userDTO);
    }

    @GetMapping("/balance/user")
    public Long getBalance(@RequestParam("userId") Long userId) {
        return userService.getBalance(userId);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

}
