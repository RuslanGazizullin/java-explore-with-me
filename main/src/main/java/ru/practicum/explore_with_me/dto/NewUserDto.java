package ru.practicum.explore_with_me.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String name;
}
