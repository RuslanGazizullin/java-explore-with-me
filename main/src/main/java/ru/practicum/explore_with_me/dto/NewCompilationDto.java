package ru.practicum.explore_with_me.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotBlank
    private String title;
}
