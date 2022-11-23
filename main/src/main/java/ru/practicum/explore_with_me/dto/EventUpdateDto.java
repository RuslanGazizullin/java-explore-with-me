package ru.practicum.explore_with_me.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventUpdateDto {
    private Long eventId;
    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    private Boolean paid;
    private Long participantLimit;
    private String title;
}
