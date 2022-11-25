package ru.practicum.explore_with_me.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.ParticipationRequestDto;
import ru.practicum.explore_with_me.exception.ValidationException;
import ru.practicum.explore_with_me.mapper.ParticipationRequestMapper;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.ParticipationRequest;
import ru.practicum.explore_with_me.model.RequestStatus;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.repository.ParticipationRequestRepository;
import ru.practicum.explore_with_me.validation.EventValidation;
import ru.practicum.explore_with_me.validation.ParticipationRequestValidation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestMapper participationRequestMapper;
    private final ParticipationRequestValidation participationRequestValidation;
    private final EventValidation eventValidation;

    @Override
    public List<ParticipationRequestDto> findAllByUserByEvent(Long userId, Long eventId) {
        eventValidation.eventInitiatorForRequestValidation(eventId, userId);
        log.info("All requests to event id {} found", eventId);
        return requestRepository.findAll()
                .stream()
                .filter(participationRequest -> participationRequest.getEvent().equals(eventId))
                .map(participationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto confirmByUser(Long userId, Long eventId, Long reqId) {
        Event event = eventValidation.eventInitiatorForRequestValidation(eventId, userId);
        ParticipationRequest participationRequest = participationRequestValidation.requestIdValidation(reqId);
        participationRequestValidation.userIsRequesterValidation(userId, participationRequest.getRequester());
        List<ParticipationRequest> requests = requestRepository.findAllByEvent(eventId);
        List<ParticipationRequest> confirmedRequests = filterEventByStatus(requests, RequestStatus.CONFIRMED);
        List<ParticipationRequest> pendingRequests = filterEventByStatus(requests, RequestStatus.PENDING);
        if (event.getParticipantLimit() <= confirmedRequests.size()) {
            pendingRequests
                    .stream()
                    .peek(x -> x.setStatus(RequestStatus.CANCELED))
                    .forEach(requestRepository::save);
            throw new ValidationException("Participant limit reached");
        } else {
            participationRequest.setStatus(RequestStatus.CONFIRMED);
            ParticipationRequest savedRequest = requestRepository.save(participationRequest);
            log.info("Request id {} to event id {} confirmed", reqId, eventId);
            return participationRequestMapper.toParticipationRequestDto(savedRequest);
        }
    }

    @Override
    public ParticipationRequestDto rejectByUser(Long userId, Long eventId, Long reqId) {
        eventValidation.eventInitiatorForRequestValidation(eventId, userId);
        ParticipationRequest participationRequest = participationRequestValidation.requestIdValidation(reqId);
        participationRequestValidation.userIsRequesterValidation(userId, participationRequest.getRequester());
        participationRequest.setStatus(RequestStatus.REJECTED);
        ParticipationRequest savedRequest = requestRepository.save(participationRequest);
        log.info("Request id {} to event id {} rejected", reqId, eventId);
        return participationRequestMapper.toParticipationRequestDto(savedRequest);
    }

    @Override
    public List<ParticipationRequestDto> findAll(Long userId) {
        List<Long> userEventsId = eventRepository.findAllIdByInitiator(userId);
        log.info("All requests found");
        return requestRepository.findAllByRequester(userId)
                .stream()
                .filter(participationRequest -> !userEventsId.contains(participationRequest.getEvent()))
                .map(participationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto add(Long userId, Long eventId) {
        Event event = eventValidation.addRequestForEventValidation(eventId, userId);
        participationRequestValidation.duplicateRequestValidation(eventId, userId);
        ParticipationRequest participationRequest = ParticipationRequest
                .builder()
                .created(LocalDateTime.now())
                .event(eventId)
                .requester(userId)
                .status(event.getRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED)
                .build();
        ParticipationRequest savedRequest = requestRepository.save(participationRequest);
        log.info("Request to event id {} added", eventId);
        return participationRequestMapper.toParticipationRequestDto(savedRequest);
    }

    @Override
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        ParticipationRequest participationRequest = participationRequestValidation.requestIdValidation(requestId);
        participationRequestValidation.userIsNotRequesterValidation(userId, participationRequest.getRequester());
        participationRequest.setStatus(RequestStatus.CANCELED);
        ParticipationRequest savedRequest = requestRepository.save(participationRequest);
        log.info("Request id {} canceled by user id {}", requestId, userId);
        return participationRequestMapper.toParticipationRequestDto(savedRequest);
    }

    @Override
    public List<ParticipationRequest> findAllByEventAndStatus(Long eventId, RequestStatus status) {
        return requestRepository.findAllByEventAndStatus(eventId, status);
    }

    private List<ParticipationRequest> filterEventByStatus(List<ParticipationRequest> requests, RequestStatus status) {
        return requests
                .stream()
                .filter(participationRequest -> participationRequest.getStatus().equals(status))
                .collect(Collectors.toList());
    }
}
