package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.dto.NewUserDto;
import ru.practicum.explore_with_me.dto.UserDto;
import ru.practicum.explore_with_me.dto.UserShortDto;

import java.util.List;

public interface UserService {

    UserDto add(NewUserDto newUserDto);

    void deleteById(Long userId);

    List<UserDto> findAll(List<Long> ids, Integer from, Integer size);

    UserShortDto findById(Long id);
}
