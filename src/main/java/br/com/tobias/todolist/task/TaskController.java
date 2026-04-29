package br.com.tobias.todolist.task;

import java.util.List;
import java.util.UUID;

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

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/task")
    public ResponseEntity<?> create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var userID = (UUID) request.getAttribute("userID");

        try {
            var task = taskService.create(taskModel, userID);
            return ResponseEntity.status(201).body(task);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/tasks")
    public List<TaskModel> getAll(HttpServletRequest request) {
        var userID = (UUID) request.getAttribute("userID");
        return taskService.findByUser(userID);
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<TaskModel> updateTask(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
        var userID = (UUID) request.getAttribute("userID");
        var updated = taskService.update(id, taskModel, userID);
        return ResponseEntity.ok(updated);
    }
}
