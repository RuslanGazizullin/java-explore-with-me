package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.dto.CommentFullDto;
import ru.practicum.explore_with_me.dto.CommentDto;
import ru.practicum.explore_with_me.dto.CommentUpdateDto;

import java.util.List;

public interface CommentService {
    CommentFullDto add(CommentDto commentDto, Long userId, Long eventId);

    CommentFullDto update(CommentUpdateDto commentDto, Long userId);

    void deleteById(Long commentId, Long userId);

    List<CommentFullDto> findAllByUser(Long userId, List<Long> events, List<String> statuses, String rangeStart,
                                       String rangeEnd, Integer from, Integer size);

    CommentFullDto findByIdByUser(Long userId, Long commentId);

    List<CommentFullDto> findAll(String text, String rangeStart, String rangeEnd, Integer from, Integer size);

    CommentFullDto findById(Long commentId);

    List<CommentFullDto> findAllByAdmin(List<Long> authors, List<Long> events, List<String> statuses,
                                        String rangeStart, String rangeEnd, Integer from, Integer size);

    CommentFullDto updateByAdmin(CommentDto commentDto, Long commentId);

    CommentFullDto publish(Long commentId);

    CommentFullDto reject(Long commentId);
}
