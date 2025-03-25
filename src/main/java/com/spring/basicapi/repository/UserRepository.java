package com.spring.basicapi.repository;

import com.spring.basicapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUser(String user);
}
