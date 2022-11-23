package ru.practicum.explore_with_me.validation;

import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.exception.ValidationException;
import ru.practicum.explore_with_me.model.ParticipationRequest;
import ru.practicum.explore_with_me.repository.ParticipationRequestRepository;

import java.util.List;

@Component
public class ParticipationRequestValidation {

    private final ParticipationRequestRepository participationRequestRepository;

    public ParticipationRequestValidation(ParticipationRequestRepository participationRequestRepository) {
        this.participationRequestRepository = participationRequestRepository;
    }

    public void userIsRequesterValidation(Long userId, Long requesterId) {
        if (userId.equals(requesterId)) {
            throw new ValidationException("User is requester");
        }
    }

    public void userIsNotRequesterValidation(Long userId, Long requesterId) {
        if (!userId.equals(requesterId)) {
            throw new ValidationException("User isn't requester");
        }
    }

    public void duplicateRequestValidation(Long eventId, Long requesterId) {
        List<ParticipationRequest> requests = participationRequestRepository.findAllByRequester(requesterId);
        for (ParticipationRequest request : requests) {
            if (request.getEvent().equals(eventId)) {
                throw new ValidationException("Duplicate request");
            }
        }
    }
}
