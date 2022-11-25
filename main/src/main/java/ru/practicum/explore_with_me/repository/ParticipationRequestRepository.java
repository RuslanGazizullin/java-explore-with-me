package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore_with_me.model.ParticipationRequest;
import ru.practicum.explore_with_me.model.RequestStatus;

import java.util.List;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByRequester(Long requesterId);

    List<ParticipationRequest> findAllByEventAndStatus(Long event, RequestStatus status);

    List<ParticipationRequest> findAllByEvent(Long event);
}
