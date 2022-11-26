package ru.practicum.explore_with_me.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EndpointHit {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
