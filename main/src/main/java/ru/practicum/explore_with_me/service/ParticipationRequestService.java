package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.dto.ParticipationRequestDto;
import ru.practicum.explore_with_me.model.ParticipationRequest;
import ru.practicum.explore_with_me.model.RequestStatus;

import java.util.List;

public interface ParticipationRequestService {
    List<ParticipationRequestDto> findAll(Long userId);

    ParticipationRequestDto add(Long userId, Long eventId);

    ParticipationRequestDto cancel(Long userId, Long requestId);

    List<ParticipationRequestDto> findAllByUserByEvent(Long userId, Long eventId);

    ParticipationRequestDto confirmByUser(Long userId, Long eventId, Long reqId);

    ParticipationRequestDto rejectByUser(Long userId, Long eventId, Long reqId);

    List<ParticipationRequest> findAllByEventAndStatus(Long eventId, RequestStatus status);
}
