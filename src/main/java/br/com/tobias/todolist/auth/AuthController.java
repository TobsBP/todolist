package br.com.tobias.todolist.auth;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.tobias.todolist.user.IUserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IUserRepository userRepository;
    private final JwtProvider jwtProvider;

    public AuthController(IUserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        var username = body.get("username");
        var password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("username and password are required");
        }

        var user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        var result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
        if (!result.verified) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        var token = jwtProvider.generate(user.getId());
        return ResponseEntity.ok(Map.of(
                "token", token,
                "tokenType", "Bearer",
                "expiresInHours", jwtProvider.getExpirationHours(),
                "userID", user.getId().toString()));
    }
}
