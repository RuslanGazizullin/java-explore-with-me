package ru.practicum.explore_with_me.validation;

import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.exception.NotFoundException;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.repository.UserRepository;

import java.util.Optional;

@Component
public class UserValidation {

    private final UserRepository userRepository;

    public UserValidation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User userIdValidation(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return user.get();
    }
}
