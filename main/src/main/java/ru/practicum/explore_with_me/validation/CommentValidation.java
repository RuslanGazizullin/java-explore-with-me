package ru.practicum.explore_with_me.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.exception.NotFoundException;
import ru.practicum.explore_with_me.exception.ValidationException;
import ru.practicum.explore_with_me.model.Comment;
import ru.practicum.explore_with_me.repository.CommentRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentValidation {

    private final CommentRepository commentRepository;

    public Comment commentIdValidation(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) {
            throw new NotFoundException("Comment not found");
        }
        return comment.get();
    }

    public Comment commentAuthorValidation(Long commentId, Long userId) {
        Comment comment = commentIdValidation(commentId);
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ValidationException("User isn't author");
        }
        return comment;
    }
}
