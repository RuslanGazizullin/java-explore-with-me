package ru.practicum.explore_with_me.validation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.exception.NotFoundException;
import ru.practicum.explore_with_me.exception.ValidationException;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.EventState;
import ru.practicum.explore_with_me.model.RequestStatus;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.repository.ParticipationRequestRepository;

import java.time.LocalDateTime;

@AllArgsConstructor
@Component
public class EventValidation {

    private final EventRepository eventRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    public void eventIdValidation(Long id) {
        if (eventRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Event not found");
        }
    }

    public void eventStateForUpdateValidation(Long eventId) {
        Event event = eventRepository.findById(eventId).get();
        if (!event.getState().equals(EventState.CANCELED) && !event.getState().equals(EventState.PENDING)) {
            throw new ValidationException("State must be \"CANCELED\" or \"PENDING\"");
        }
    }

    public void eventDateValidation(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Event date should be no earlier than 2 hours later");
        }
    }

    public void eventInitiatorValidation(Long eventId, Long userId) {
        if (!eventRepository.findById(eventId).get().getInitiator().equals(userId)) {
            throw new ValidationException("User isn't event's initiator");
        }
    }

    public void addRequestEventInitiatorValidation(Long eventId, Long userId) {
        if (eventRepository.findById(eventId).get().getInitiator().equals(userId)) {
            throw new ValidationException("User is event's initiator");
        }
    }

    public void addRequestNonPublishedEventValidation(Long eventId) {
        if (!eventRepository.findById(eventId).get().getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Event must be published");
        }
    }

    public void addRequestReachLimitValidation(Long eventId) {
        if (eventRepository.findById(eventId).get().getParticipantLimit() <= participationRequestRepository
                .findAllByEventAndStatus(eventId, RequestStatus.CONFIRMED).size()) {
            throw new ValidationException("Participant limit reached");
        }
    }

    public void publishEventValidation(Long eventId) {
        Event event = eventRepository.findById(eventId).get();
        if (LocalDateTime.now().plusHours(1).isAfter(event.getEventDate())) {
            throw new ValidationException("Event date should be no earlier than 1 hours later");
        }
        if (!event.getState().equals(EventState.PENDING)) {
            throw new ValidationException("Event state must be \"PENDING\"");
        }
    }

    public void rejectEventValidation(Long eventId) {
        if (eventRepository.findById(eventId).get().getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Event already published");
        }
    }

    public void eventForUpdateValidation(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId).get();
        eventStateForUpdateValidation(eventId);
        eventDateValidation(event.getEventDate());
        eventInitiatorValidation(eventId, userId);
    }

    public void addRequestForEventValidation(Long eventId, Long userId) {
        eventIdValidation(eventId);
        addRequestEventInitiatorValidation(eventId, userId);
        addRequestNonPublishedEventValidation(eventId);
        addRequestReachLimitValidation(eventId);
    }
}
