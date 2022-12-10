package ru.practicum.explore_with_me.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.CommentDto;
import ru.practicum.explore_with_me.dto.CommentFullDto;
import ru.practicum.explore_with_me.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/admin/comments")
public class AdminCommentController {

    private final CommentService commentService;

    public AdminCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentFullDto> findAll(@RequestParam(required = false) List<Long> authors,
                                        @RequestParam(required = false) List<Long> events,
                                        @RequestParam(required = false) List<String> statuses,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                        @Positive @RequestParam(defaultValue = "100") Integer size) {
        return commentService.findAllByAdmin(authors, events, statuses, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{commentId}")
    public CommentFullDto update(@RequestBody @Valid CommentDto commentDto, @PathVariable Long commentId) {
        return commentService.updateByAdmin(commentDto, commentId);
    }

    @PatchMapping("/{commentId}/publish")
    public CommentFullDto publish(@PathVariable Long commentId) {
        return commentService.publish(commentId);
    }

    @PatchMapping("/{commentId}/reject")
    public CommentFullDto reject(@PathVariable Long commentId) {
        return commentService.reject(commentId);
    }
}
