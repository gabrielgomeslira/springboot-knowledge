package com.spring.basicapi.service;

import com.spring.basicapi.dto.TaskDTO;
import com.spring.basicapi.model.Task;
import com.spring.basicapi.model.User;
import com.spring.basicapi.repository.TaskRepo;
import com.spring.basicapi.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        task.setValue(taskDTO.getValue());
        task.setUser(user);
        return taskRepo.save(task);

    }

    public List<Task> getAllTasks(){ return taskRepo.findAll();
    }

    public Task getTaskById(Long id) {
        Optional<Task> task = taskRepo.findById(id);
        return task.orElse(null);
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
