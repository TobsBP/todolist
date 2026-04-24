package br.com.tobias.todolist.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/users")
public class UserController {
    
    @PostMapping("/user")
    public void create(@RequestBody UserModel userModel) {
        System.out.println(userModel.name);
    }
}