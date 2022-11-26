package ru.practicum.explore_with_me.dto;

import lombok.*;
import ru.practicum.explore_with_me.model.RequestStatus;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {
    private Long id;
    private String created;
    private Long event;
    private Long requester;
    private RequestStatus status;
}
