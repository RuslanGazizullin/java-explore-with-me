package ru.practicum.explore_with_me.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.ParticipationRequestDto;
import ru.practicum.explore_with_me.model.ParticipationRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ParticipationRequestMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .created(participationRequest.getCreated().format(FORMATTER))
                .event(participationRequest.getEvent())
                .requester(participationRequest.getRequester())
                .status(participationRequest.getStatus())
                .build();
    }

    public ParticipationRequest fromParticipationRequestDto(ParticipationRequestDto participationRequestDto) {
        return ParticipationRequest.builder()
                .id(participationRequestDto.getId())
                .created(LocalDateTime.parse(participationRequestDto.getCreated(), FORMATTER))
                .event(participationRequestDto.getEvent())
                .requester(participationRequestDto.getRequester())
                .status(participationRequestDto.getStatus())
                .build();
    }
}
