package ru.practicum.explore_with_me.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventShortDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private String eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Integer views;
    private Integer confirmedRequests;
}
