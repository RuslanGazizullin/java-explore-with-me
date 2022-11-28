package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.CommentFullDto;
import ru.practicum.explore_with_me.dto.CommentDto;
import ru.practicum.explore_with_me.dto.CommentUpdateDto;
import ru.practicum.explore_with_me.exception.NotFoundException;
import ru.practicum.explore_with_me.mapper.CommentMapper;
import ru.practicum.explore_with_me.model.Comment;
import ru.practicum.explore_with_me.model.CommentStatus;
import ru.practicum.explore_with_me.repository.CommentRepository;
import ru.practicum.explore_with_me.validation.CommentValidation;
import ru.practicum.explore_with_me.validation.EventValidation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final CommentValidation commentValidation;
    private final EventValidation eventValidation;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public CommentFullDto add(CommentDto commentDto, Long userId, Long eventId) {
        eventValidation.eventIdValidation(eventId);
        Comment comment = commentMapper.fromCommentDto(commentDto);
        comment.setAuthor(userId);
        comment.setEventId(eventId);
        Comment savedComment = commentRepository.save(comment);
        log.info("Comment to event {} added", eventId);
        return commentMapper.toCommentFullDto(savedComment);
    }

    @Override
    public CommentFullDto update(CommentUpdateDto commentDto, Long userId) {
        Long commentId = commentDto.getId();
        Comment oldComment = commentValidation.commentAuthorValidation(commentId, userId);
        oldComment.setText(commentDto.getText());
        if (!oldComment.getStatus().equals(CommentStatus.PENDING)) {
            oldComment.setStatus(CommentStatus.PENDING);
        }
        Comment updatedComment = commentRepository.save(oldComment);
        log.info("Comment {} to event {} updated by author", commentId, oldComment.getEventId());
        return commentMapper.toCommentFullDto(updatedComment);
    }

    @Override
    public void deleteById(Long commentId, Long userId) {
        commentValidation.commentAuthorValidation(commentId, userId);
        log.info("Comment {} deleted", commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentFullDto> findAllByUser(Long userId, List<Long> events, List<String> statuses, String rangeStart,
                                              String rangeEnd, Integer from, Integer size) {
        List<CommentFullDto> comments;
        if (rangeStart != null && rangeEnd != null) {
            comments = commentRepository.findAllByAuthor(userId, events, LocalDateTime.parse(rangeStart, FORMATTER),
                            LocalDateTime.parse(rangeEnd, FORMATTER), PageRequest.of(from / size, size))
                    .stream()
                    .filter(comment -> statuses.contains(comment.getStatus().name()))
                    .map(commentMapper::toCommentFullDto)
                    .collect(Collectors.toList());
        } else {
            comments = commentRepository.findAllByAuthor(userId, events, LocalDateTime.now(),
                            PageRequest.of(from / size, size))
                    .stream()
                    .filter(comment -> statuses.contains(comment.getStatus().name()))
                    .map(commentMapper::toCommentFullDto)
                    .collect(Collectors.toList());
        }
        log.info("All required comments found");
        return comments;
    }

    @Override
    public CommentFullDto findByIdByUser(Long userId, Long commentId) {
        Comment comment = commentValidation.commentAuthorValidation(commentId, userId);
        log.info("Comment {} found", commentId);
        return commentMapper.toCommentFullDto(comment);
    }

    @Override
    public List<CommentFullDto> findAll(String text, String rangeStart, String rangeEnd, Integer from, Integer size) {
        List<CommentFullDto> comments;
        if (rangeStart != null && rangeEnd != null) {
            comments = commentRepository.findAll(text, LocalDateTime.parse(rangeStart, FORMATTER),
                            LocalDateTime.parse(rangeEnd, FORMATTER), PageRequest.of(from / size, size))
                    .stream()
                    .filter(comment -> comment.getStatus().equals(CommentStatus.PUBLISHED))
                    .map(commentMapper::toCommentFullDto)
                    .collect(Collectors.toList());
        } else {
            comments = commentRepository.findAll(text, LocalDateTime.now(), PageRequest.of(from / size, size))
                    .stream()
                    .filter(comment -> comment.getStatus().equals(CommentStatus.PUBLISHED))
                    .map(commentMapper::toCommentFullDto)
                    .collect(Collectors.toList());
        }
        log.info("All required comments found");
        return comments;
    }

    @Override
    public CommentFullDto findById(Long commentId) {
        Comment comment = commentValidation.commentIdValidation(commentId);
        if (comment.getStatus().equals(CommentStatus.PUBLISHED)) {
            log.info("Comment {} found", commentId);
            return commentMapper.toCommentFullDto(comment);
        } else {
            throw new NotFoundException("Comment not found");
        }
    }

    @Override
    public List<CommentFullDto> findAllByAdmin(List<Long> authors, List<Long> events, String rangeStart,
                                               String rangeEnd, Integer from, Integer size) {
        List<CommentFullDto> comments;
        if (rangeStart != null && rangeEnd != null) {
            comments = commentRepository.findAll(authors, events, LocalDateTime.parse(rangeStart, FORMATTER),
                            LocalDateTime.parse(rangeEnd, FORMATTER), PageRequest.of(from / size, size))
                    .stream()
                    .map(commentMapper::toCommentFullDto)
                    .collect(Collectors.toList());
        } else {
            comments = commentRepository.findAll(authors, events, LocalDateTime.now(),
                            PageRequest.of(from / size, size))
                    .stream()
                    .map(commentMapper::toCommentFullDto)
                    .collect(Collectors.toList());
        }
        log.info("All required comments found by admin");
        return comments;
    }

    @Override
    public CommentFullDto updateByAdmin(CommentDto commentDto, Long commentId) {
        Comment comment = commentValidation.commentIdValidation(commentId);
        comment.setText(commentDto.getText());
        Comment updatedComment = commentRepository.save(comment);
        log.info("Comment {} updated by admin", commentId);
        return commentMapper.toCommentFullDto(updatedComment);
    }

    @Override
    public CommentFullDto publish(Long commentId) {
        Comment comment = commentValidation.commentIdValidation(commentId);
        comment.setStatus(CommentStatus.PUBLISHED);
        comment.setPublishedOn(LocalDateTime.now());
        Comment publishedComment = commentRepository.save(comment);
        log.info("Comment {} published", commentId);
        return commentMapper.toCommentFullDto(publishedComment);
    }

    @Override
    public CommentFullDto reject(Long commentId) {
        Comment comment = commentValidation.commentIdValidation(commentId);
        comment.setStatus(CommentStatus.CANCELED);
        Comment rejectedComment = commentRepository.save(comment);
        log.info("Comment {} published", commentId);
        return commentMapper.toCommentFullDto(rejectedComment);
    }
}