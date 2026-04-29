package br.com.tobias.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    
    @Autowired
    private ITaskRepository taskRepository;
    
    public void validateDates(TaskModel taskModel) {
        var now = LocalDateTime.now();
    
        if (taskModel.getStartAt() == null || taskModel.getEndAt() == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }
    
        if (now.isAfter(taskModel.getStartAt()) || now.isAfter(taskModel.getEndAt())) {
            throw new IllegalArgumentException("Dates must be in the future");
        }
    
        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }
    
    @PostMapping("/task")
    public ResponseEntity<?> create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        
        var userID = request.getAttribute("userID");
        
        taskModel.setUserID((UUID) userID);
        
        try {
            validateDates(taskModel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        
        this.taskRepository.save(taskModel);  
        
        return ResponseEntity.status(201).body(taskModel);
    }

    @GetMapping("/tasks")
    public List<TaskModel> getAll(HttpServletRequest request) {
        var userID = request.getAttribute("userID");
        System.out.println(userID);

        var tasks = this.taskRepository.findByUserID((UUID) userID);
        
        return tasks;
    }
    
    @PutMapping("/task/{id}")
    public ResponseEntity<TaskModel> updateTask(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id){
        var userID = request.getAttribute("userID");
        
        taskModel.setUserID((UUID) userID);
        taskModel.setId(id);
        
        this.taskRepository.save(taskModel);
        
        return ResponseEntity.status(200).body(taskModel);
    }
}