package br.com.tobias.todolist.user;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {
        var userID = (UUID) request.getAttribute("userID");
        var user = userService.findById(userID);
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/me")
    public ResponseEntity<?> updateTheme(@RequestBody Map<String, String> body, HttpServletRequest request) {
        var userID = (UUID) request.getAttribute("userID");
        var user = userService.updateTheme(userID, body.get("theme"));
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body, HttpServletRequest request) {
        try {
            var userID = (UUID) request.getAttribute("userID");
            userService.changePassword(userID, body.get("currentPassword"), body.get("newPassword"));
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/user")
    public ResponseEntity<?> create(@RequestBody UserModel userModel) {
        try {
            var user = userService.create(userModel);
            return ResponseEntity.status(201).body(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
