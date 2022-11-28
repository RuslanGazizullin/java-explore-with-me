package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.CommentFullDto;
import ru.practicum.explore_with_me.dto.CommentDto;
import ru.practicum.explore_with_me.dto.CommentUpdateDto;
import ru.practicum.explore_with_me.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users/{userId}/comments")
public class PrivateCommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentFullDto add(@RequestBody @Valid CommentDto commentDto,
                              @PathVariable Long userId,
                              @RequestParam Long eventId) {
        return commentService.add(commentDto, userId, eventId);
    }

    @PatchMapping
    public CommentFullDto update(@RequestBody @Valid CommentUpdateDto commentDto, @PathVariable Long userId) {
        return commentService.update(commentDto, userId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteById(@PathVariable Long commentId,
                           @PathVariable Long userId) {
        commentService.deleteById(commentId, userId);
    }

    @GetMapping
    public List<CommentFullDto> findAll(@PathVariable Long userId,
                                        @RequestParam(defaultValue = "[]") List<Long> events,
                                        @RequestParam(defaultValue = "[]") List<String> statuses,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                        @Positive @RequestParam(defaultValue = "100") Integer size) {
        return commentService.findAllByUser(userId, events, statuses, rangeStart, rangeEnd, from, size);
    }

    @GetMapping("/{commentId}")
    public CommentFullDto findById(@PathVariable Long commentId,
                                   @PathVariable Long userId) {
        return commentService.findByIdByUser(userId, commentId);
    }
}
