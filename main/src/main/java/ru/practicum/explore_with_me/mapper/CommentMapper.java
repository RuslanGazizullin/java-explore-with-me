package ru.practicum.explore_with_me.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.CommentFullDto;
import ru.practicum.explore_with_me.dto.CommentDto;
import ru.practicum.explore_with_me.model.Comment;
import ru.practicum.explore_with_me.model.CommentStatus;
import ru.practicum.explore_with_me.service.EventService;
import ru.practicum.explore_with_me.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final UserService userService;
    private final EventService eventService;
    private final EventMapper eventMapper;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CommentFullDto toCommentFullDto(Comment comment) {
        return CommentFullDto
                .builder()
                .id(comment.getId())
                .event(eventMapper.toEventShortDto(eventService.findByIdForMapper(comment.getEventId())))
                .author(userService.findById(comment.getAuthor()))
                .text(comment.getText())
                .createdOn(comment.getCreatedOn().format(FORMATTER))
                .publishedOn(comment.getPublishedOn() == null ? null : comment.getPublishedOn().format(FORMATTER))
                .status(comment.getStatus())
                .build();
    }

    public Comment fromCommentDto(CommentDto commentDto) {
        return Comment
                .builder()
                .text(commentDto.getText())
                .createdOn(LocalDateTime.now())
                .status(CommentStatus.PENDING)
                .build();
    }
}
