package ru.practicum.explore_with_me.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.ParticipationRequestDto;
import ru.practicum.explore_with_me.model.ParticipationRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ParticipationRequestMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .created(participationRequest.getCreated().format(formatter))
                .event(participationRequest.getEvent())
                .requester(participationRequest.getRequester())
                .status(participationRequest.getStatus())
                .build();
    }

    public ParticipationRequest fromParticipationRequestDto(ParticipationRequestDto participationRequestDto) {
        return ParticipationRequest.builder()
                .id(participationRequestDto.getId())
                .created(LocalDateTime.parse(participationRequestDto.getCreated(), formatter))
                .event(participationRequestDto.getEvent())
                .requester(participationRequestDto.getRequester())
                .status(participationRequestDto.getStatus())
                .build();
    }
}
