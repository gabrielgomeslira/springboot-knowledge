package com.spring.basicapi.controller;
import com.spring.basicapi.dto.TaskDTO;
import com.spring.basicapi.model.Task;
import com.spring.basicapi.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public Task createTask(@RequestBody TaskDTO taskDTO){
        return Optional.ofNullable(taskService.createTask(taskDTO))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ERRO WHEN CREATE TASK!"));
    }

    @GetMapping("/list")
    public List<Task> getTasks(){
        return taskService.getAllTasks();
    }

    @GetMapping("/")
    public Task getTaskById(@RequestBody Long id){
        return Optional.ofNullable(taskService.getTaskById(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ERRO WHEN GET TASK"));
    }

    @DeleteMapping("/delete/{id}")
    public Task deleteTaskById(@PathVariable Long id){
        if (!taskService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND");
        }
        return taskService.deleteTaskById(id);
    }
}
