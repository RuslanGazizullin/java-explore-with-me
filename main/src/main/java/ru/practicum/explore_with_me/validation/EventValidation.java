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
import java.util.Optional;

@AllArgsConstructor
@Component
public class EventValidation {

    private final EventRepository eventRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    public Event eventIdValidation(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isEmpty()) {
            throw new NotFoundException("Event not found");
        }
        return event.get();
    }

    public Event eventIdAndInitiatorValidation(Long eventId, Long initiatorId) {
        Optional<Event> event = eventRepository.findByIdAndInitiator(eventId, initiatorId);
        if (event.isEmpty()) {
            throw new NotFoundException("Event not found");
        } else {
            return event.get();
        }
    }

    public void eventStateForUpdateValidation(Event event) {
        if (!event.getState().equals(EventState.CANCELED) && !event.getState().equals(EventState.PENDING)) {
            throw new ValidationException("State must be \"CANCELED\" or \"PENDING\"");
        }
    }

    public void eventDateValidation(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Event date should be no earlier than 2 hours later");
        }
    }

    public void eventInitiatorValidation(Event event, Long userId) {
        if (!event.getInitiator().equals(userId)) {
            throw new ValidationException("User isn't event's initiator");
        }
    }

    public Event eventInitiatorForRequestValidation(Long eventId, Long userId) {
        Event event = eventIdValidation(eventId);
        if (!event.getInitiator().equals(userId)) {
            throw new ValidationException("User isn't event's initiator");
        }
        return event;
    }

    public void addRequestEventInitiatorValidation(Event event, Long userId) {
        if (event.getInitiator().equals(userId)) {
            throw new ValidationException("User is event's initiator");
        }
    }

    public void addRequestNonPublishedEventValidation(Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Event must be published");
        }
    }

    public void addRequestReachLimitValidation(Event event) {
        if (event.getParticipantLimit() <= participationRequestRepository
                .findAllByEventAndStatus(event.getId(), RequestStatus.CONFIRMED).size()) {
            throw new ValidationException("Participant limit reached");
        }
    }

    public Event publishEventValidation(Long eventId) {
        Event event = eventIdValidation(eventId);
        if (LocalDateTime.now().plusHours(1).isAfter(event.getEventDate())) {
            throw new ValidationException("Event date should be no earlier than 1 hours later");
        }
        if (!event.getState().equals(EventState.PENDING)) {
            throw new ValidationException("Event state must be \"PENDING\"");
        }
        return event;
    }

    public Event rejectEventValidation(Long eventId) {
        Event event = eventIdValidation(eventId);
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Event already published");
        }
        return event;
    }

    public void eventForUpdateValidation(Long eventId, Long userId) {
        Event event = eventIdValidation(eventId);
        eventStateForUpdateValidation(event);
        eventDateValidation(event.getEventDate());
        eventInitiatorValidation(event, userId);
    }

    public Event addRequestForEventValidation(Long eventId, Long userId) {
        Event event = eventIdValidation(eventId);
        addRequestEventInitiatorValidation(event, userId);
        addRequestNonPublishedEventValidation(event);
        addRequestReachLimitValidation(event);
        return event;
    }
}
