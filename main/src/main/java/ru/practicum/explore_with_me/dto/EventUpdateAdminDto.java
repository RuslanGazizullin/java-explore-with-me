package ru.practicum.explore_with_me.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventUpdateAdminDto {
    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    private String title;
}
