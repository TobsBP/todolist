package br.com.tobias.todolist.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.tobias.todolist.user.IUserRepository;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    
    @Autowired
    private ITaskRepository taskRepository;

    @Autowired
    private IUserRepository userRepository;
    
    @PostMapping("/task")
    public ResponseEntity<?> create(@RequestBody TaskModel taskModel) {
        
        var userID = taskModel.getUserID();
        var user = this.userRepository.findById(userID);
        
        if (user.isEmpty()) {
            return ResponseEntity.status(400).body("This userID doesn't exist!");
        }
        
        this.taskRepository.save(taskModel);  
        
        return ResponseEntity.status(201).body(taskModel);
    }
}