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

    private final ParticipationRequestRepository participationRequestRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestMapper participationRequestMapper;
    private final ParticipationRequestValidation participationRequestValidation;
    private final EventValidation eventValidation;

    @Override
    public List<ParticipationRequestDto> findAllByUserByEvent(Long userId, Long eventId) {
        eventValidation.eventInitiatorValidation(eventId, userId);
        return participationRequestRepository.findAll()
                .stream()
                .filter(participationRequest -> participationRequest.getEvent().equals(eventId))
                .map(participationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto confirmByUser(Long userId, Long eventId, Long reqId) {
        eventValidation.eventInitiatorValidation(eventId, userId);
        Event event = eventRepository.findById(eventId).get();
        ParticipationRequest participationRequest = participationRequestRepository.findById(reqId).get();
        participationRequestValidation.userIsRequesterValidation(userId, participationRequest.getRequester());
        if (event.getParticipantLimit() <= participationRequestRepository
                .findAllByEventAndStatus(eventId, RequestStatus.CONFIRMED).size()) {
            participationRequestRepository.findAllByEventAndStatus(eventId, RequestStatus.PENDING)
                    .stream()
                    .peek(x -> x.setStatus(RequestStatus.CANCELED))
                    .forEach(participationRequestRepository::save);
            throw new ValidationException("Participant limit reached");
        } else {
            participationRequest.setStatus(RequestStatus.CONFIRMED);
            return participationRequestMapper.toParticipationRequestDto(participationRequestRepository
                    .save(participationRequest));
        }
    }

    @Override
    public ParticipationRequestDto rejectByUser(Long userId, Long eventId, Long reqId) {
        eventValidation.eventInitiatorValidation(eventId, userId);
        ParticipationRequest participationRequest = participationRequestRepository.findById(reqId).get();
        participationRequestValidation.userIsRequesterValidation(userId, participationRequest.getRequester());
        participationRequest.setStatus(RequestStatus.REJECTED);
        return participationRequestMapper.toParticipationRequestDto(participationRequestRepository
                .save(participationRequest));
    }

    @Override
    public List<ParticipationRequestDto> findAll(Long userId) {
        List<Long> userEventsId = eventRepository.findAllIdByInitiator(userId);
        return participationRequestRepository.findAllByRequester(userId)
                .stream()
                .filter(participationRequest -> !userEventsId.contains(participationRequest.getEvent()))
                .map(participationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto add(Long userId, Long eventId) {
        eventValidation.addRequestForEventValidation(eventId, userId);
        participationRequestValidation.duplicateRequestValidation(eventId, userId);
        ParticipationRequest participationRequest = ParticipationRequest
                .builder()
                .created(LocalDateTime.now())
                .event(eventId)
                .requester(userId)
                .status(!eventRepository.findById(eventId).get()
                        .getRequestModeration() ? RequestStatus.CONFIRMED : RequestStatus.PENDING)
                .build();
        return participationRequestMapper.toParticipationRequestDto(participationRequestRepository
                .save(participationRequest));
    }

    @Override
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        ParticipationRequest participationRequest = participationRequestRepository.findById(requestId).get();
        participationRequestValidation.userIsNotRequesterValidation(userId, participationRequest.getRequester());
        participationRequest.setStatus(RequestStatus.CANCELED);
        return participationRequestMapper.toParticipationRequestDto(participationRequestRepository
                .save(participationRequest));
    }
}
