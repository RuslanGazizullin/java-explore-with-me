package ru.practicum.explore_with_me.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.NewUserDto;
import ru.practicum.explore_with_me.dto.UserDto;
import ru.practicum.explore_with_me.dto.UserShortDto;
import ru.practicum.explore_with_me.model.User;

@Component
public class UserMapper {
    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

    public User fromNewUserDto(NewUserDto newUserDto) {
        return new User(null, newUserDto.getName(), newUserDto.getEmail());
    }
}
