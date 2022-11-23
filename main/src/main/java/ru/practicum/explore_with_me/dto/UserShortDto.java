package ru.practicum.explore_with_me.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserShortDto {
    private Long id;
    @NotBlank
    private String name;
}
