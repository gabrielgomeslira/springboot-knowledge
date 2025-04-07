package com.spring.basicapi.service;

import com.spring.basicapi.dto.TaskDTO;
import com.spring.basicapi.model.Task;
import com.spring.basicapi.model.User;
import com.spring.basicapi.repository.TaskRepo;
import com.spring.basicapi.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class TaskService {

    @Autowired
    TaskRepo taskRepo;

    @Autowired
    UserRepo userRepo;

    public Task createTask(TaskDTO taskDTO){
        User user = userRepo.findById(taskDTO.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found!"));
        Task task = new Task();
        task.setDescription(taskDTO.getDescription());
        Random random = new Random();
        long randomValue = random.nextLong(10, 26);
        task.setValue(randomValue);
        task.setStatus(false);
        task.setUser(user);
        return taskRepo.save(task);

    }


    public List<Task> getAllTasks(){ return taskRepo.findAll();
    }

    @Transactional
    public Task markTaskAsCompleted(Long userId) {
        System.out.println(">>> markTaskAsCompleted CALLED");
        Task task = taskRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + userId));
        User user = task.getUser();
        long taskValue = task.getValue();
        user.setBalance(user.getBalance() + taskValue);
        task.setStatus(true);
        userRepo.save(user);
        return taskRepo.save(task);
    }

    public List<Task> getTasksByUserId(Long userId) {
        return taskRepo.findByUserId(userId);
    }

    public Task deleteTaskById(Long id) {
        if (!taskRepo.existsById(id)) {
            throw new RuntimeException("Task not found with ID: " + id);
        }
        taskRepo.deleteById(id);
        return null;
    }


    public boolean existsById(Long id) {
        if (taskRepo.existsById(id)) {
            return true;
        } else
            return false;
    }
}
