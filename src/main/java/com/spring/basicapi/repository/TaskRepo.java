package com.spring.basicapi.repository;

import com.spring.basicapi.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<Task,Long> {
}
