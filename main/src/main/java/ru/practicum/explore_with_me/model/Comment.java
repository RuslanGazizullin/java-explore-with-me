package ru.practicum.explore_with_me.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "event_id")
    private Long eventId;
    @NotNull
    @Column(name = "author")
    private Long author;
    @NotBlank
    @Column(name = "text")
    private String text;
    @NotNull
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CommentStatus status;
}
