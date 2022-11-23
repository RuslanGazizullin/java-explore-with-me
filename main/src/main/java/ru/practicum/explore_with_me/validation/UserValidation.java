package ru.practicum.explore_with_me.validation;

import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.exception.NotFoundException;
import ru.practicum.explore_with_me.repository.UserRepository;

@Component
public class UserValidation {

    private final UserRepository userRepository;

    public UserValidation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void userIdValidation(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("User not found");
        }
    }
}
