package com.spring.basicapi.service;

import com.spring.basicapi.dto.LoginResponse;
import com.spring.basicapi.dto.UserDTO;
import com.spring.basicapi.model.User;
import com.spring.basicapi.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private View error;

    public User createUser(UserDTO userDTO) {
        for (User user : userRepo.findAll()){
            if (user.getUsername().equals(userDTO.getUsername())){
                throw new RuntimeException("Username already registered!");
            }
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setBalance(0L);
        return userRepo.save(user);
    }

    public ResponseEntity<LoginResponse> loginUser(UserDTO userDTO) {
        for (User user : userRepo.findAll()) {
            if (user.getUsername().equals(userDTO.getUsername()) &&
                    user.getPassword().equals(userDTO.getPassword())) {

                LoginResponse response = new LoginResponse(user.getId(), user.getUsername());
                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Long getBalance(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return user.getBalance();
    }


    public User getUserById(Long id) {
        Optional<User> user = userRepo.findById(id);
        return user.orElse(null);
    }

}
