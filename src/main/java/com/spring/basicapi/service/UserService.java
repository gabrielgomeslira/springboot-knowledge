package com.spring.basicapi.service;

import com.spring.basicapi.dto.LoginResponse;
import com.spring.basicapi.dto.UserDTO;
import com.spring.basicapi.model.User;
import com.spring.basicapi.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
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

    public User getUserById(Long id) {
        Optional<User> user = userRepo.findById(id);
        return user.orElse(null);
    }

}
