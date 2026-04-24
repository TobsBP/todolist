package br.com.tobias.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/user")
    public UserModel create(@RequestBody UserModel userModel) {
        var username = this.userRepository.findByUsername(userModel.getUsername());

        if (username != null) {
            return null;
        }

        var userCreated = this.userRepository.save(userModel);
        return  userCreated;
    }
}