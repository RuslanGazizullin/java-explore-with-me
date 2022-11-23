package ru.practicum.explore_with_me.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.NewUserDto;
import ru.practicum.explore_with_me.dto.UserDto;
import ru.practicum.explore_with_me.mapper.UserMapper;
import ru.practicum.explore_with_me.repository.UserRepository;
import ru.practicum.explore_with_me.validation.UserValidation;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidation userValidation;

    @Override
    public UserDto add(NewUserDto newUserDto) {
        return userMapper.toUserDto(userRepository.save(userMapper.fromNewUserDto(newUserDto)));
    }

    @Override
    public void deleteById(Long userId) {
        userValidation.userIdValidation(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> findAll(List<Long> ids, Integer from, Integer size) {
        if (ids == null) {
            return userRepository.findAll(PageRequest.of(from / size, size))
                    .stream()
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAll(PageRequest.of(from / size, size))
                    .stream()
                    .filter(user -> ids.contains(user.getId()))
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }
}
