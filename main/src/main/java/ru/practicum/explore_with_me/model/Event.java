package ru.practicum.explore_with_me.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "annotation")
    private String annotation;
    @Column(name = "category")
    private Long category;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "description")
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "initiator")
    private Long initiator;
    @Column(name = "location")
    private Long location;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participant_limit")
    private Long participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Column(name = "title")
    private String title;
    @Column(name = "state")
    private EventState state;
}
