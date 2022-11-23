package ru.practicum.explore_with_me.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewStats {
    private String app;
    private String uri;
    private Integer hits;
}
