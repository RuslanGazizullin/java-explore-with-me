package ru.practicum.explore_with_me.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewEventDto {
    @NotBlank
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank
    private String description;
    @NotBlank
    private String eventDate;
    @NotNull
    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    @NotBlank
    private String title;
}
