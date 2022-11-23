package ru.practicum.explore_with_me.dto;

import lombok.*;
import ru.practicum.explore_with_me.model.EventState;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventFullDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private String createdOn;
    private String description;
    private String eventDate;
    private UserShortDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private String title;
    private EventState state;
    private Integer views;
    private Integer confirmedRequests;
}
