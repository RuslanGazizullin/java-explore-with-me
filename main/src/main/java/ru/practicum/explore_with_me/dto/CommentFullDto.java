package ru.practicum.explore_with_me.dto;

import lombok.*;
import ru.practicum.explore_with_me.model.CommentStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentFullDto {
    private Long id;
    @NotNull
    private EventShortDto event;
    @NotNull
    private UserShortDto author;
    @NotBlank
    private String text;
    @NotNull
    private String createdOn;
    private String publishedOn;
    @NotNull
    private CommentStatus status;
}
