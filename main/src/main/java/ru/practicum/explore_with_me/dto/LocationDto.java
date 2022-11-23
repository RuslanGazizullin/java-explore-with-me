package ru.practicum.explore_with_me.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private Float lat;
    private Float lon;
}
