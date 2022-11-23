package ru.practicum.explore_with_me.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.NewUserDto;
import ru.practicum.explore_with_me.dto.UserDto;
import ru.practicum.explore_with_me.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto add(@RequestBody @Valid NewUserDto newUserDto) {
        return userService.add(newUserDto);
    }

    @GetMapping
    public List<UserDto> findAll(@RequestParam(required = false) List<Long> ids,
                                 @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                 @Positive @RequestParam(defaultValue = "100") Integer size) {
        return userService.findAll(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable Long userId) {
        userService.deleteById(userId);
    }
}
