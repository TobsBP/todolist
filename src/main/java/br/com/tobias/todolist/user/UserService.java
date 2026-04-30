package br.com.tobias.todolist.user;

import java.util.UUID;

import org.springframework.stereotype.Service;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service
public class UserService {

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    public UserModel updateTheme(UUID id, String theme) {
        var user = findById(id);
        user.setTheme(theme);
        return userRepository.save(user);
    }

    public void changePassword(UUID id, String currentPassword, String newPassword) {
        var user = findById(id);
        var result = BCrypt.verifyer().verify(currentPassword.toCharArray(), user.getPassword());
        if (!result.verified) {
            throw new IllegalArgumentException("Current password is wrong!");
        }
        user.setPassword(BCrypt.withDefaults().hashToString(12, newPassword.toCharArray()));
        userRepository.save(user);
    }

    public UserModel create(UserModel userModel) {
        if (userRepository.findByUsername(userModel.getUsername()) != null) {
            throw new IllegalArgumentException("User already exists!");
        }

        var hash = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(hash);

        return userRepository.save(userModel);
    }
}
