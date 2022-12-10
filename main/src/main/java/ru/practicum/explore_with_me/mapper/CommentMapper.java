package ru.practicum.explore_with_me.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.CommentFullDto;
import ru.practicum.explore_with_me.dto.CommentDto;
import ru.practicum.explore_with_me.model.Comment;
import ru.practicum.explore_with_me.model.CommentStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final EventMapper eventMapper;
    private final UserMapper userMapper;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CommentFullDto toCommentFullDto(Comment comment) {
        return CommentFullDto
                .builder()
                .id(comment.getId())
                .event(eventMapper.toEventShortDto(comment.getEvent()))
                .author(userMapper.toUserShortDto(comment.getAuthor()))
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
