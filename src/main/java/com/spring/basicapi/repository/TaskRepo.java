package com.spring.basicapi.repository;

import com.spring.basicapi.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task,Long> {
    List<Task> findByUserId(Long userId);

    Long id(Long id);
}
