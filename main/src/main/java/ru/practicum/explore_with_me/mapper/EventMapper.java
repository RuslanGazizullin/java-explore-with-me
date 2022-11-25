package ru.practicum.explore_with_me.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.client.StatsClient;
import ru.practicum.explore_with_me.dto.*;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.EventState;
import ru.practicum.explore_with_me.model.RequestStatus;
import ru.practicum.explore_with_me.service.*;
import ru.practicum.explore_with_me.validation.EventValidation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final LocationService locationService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ParticipationRequestService requestService;
    private final EventValidation eventValidation;
    private final StatsClient statsClient;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Event fromNewEventDto(NewEventDto newEventDto) {
        Long location = locationService.add(newEventDto.getLocation()).getId();
        return Event
                .builder()
                .annotation(newEventDto.getAnnotation())
                .category(newEventDto.getCategory())
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER))
                .location(location)
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .state(EventState.PENDING)
                .build();
    }

    public Event fromEventUpdateAdminDto(EventUpdateAdminDto dto, Long oldEvenId) {
        Event oldEvent = eventValidation.eventIdValidation(oldEvenId);
        return Event
                .builder()
                .id(oldEvenId)
                .annotation(dto.getAnnotation() != null ? dto.getAnnotation() : oldEvent.getAnnotation())
                .category(dto.getCategory() != null ? dto.getCategory() : oldEvent.getCategory())
                .createdOn(oldEvent.getCreatedOn())
                .description(dto.getDescription() != null ? dto.getDescription() : oldEvent.getDescription())
                .eventDate(dto.getEventDate() != null ? LocalDateTime.parse(dto.getEventDate(), FORMATTER)
                        : oldEvent.getEventDate())
                .initiator(oldEvent.getInitiator())
                .location(dto.getLocation() != null ? locationService.add(dto.getLocation()).getId()
                        : oldEvent.getLocation())
                .paid(dto.getPaid() != null ? dto.getPaid() : oldEvent.getPaid())
                .participantLimit(dto.getParticipantLimit() != null ? dto.getParticipantLimit()
                        : oldEvent.getParticipantLimit())
                .publishedOn(oldEvent.getPublishedOn())
                .requestModeration(dto.getRequestModeration() != null ? dto.getRequestModeration()
                        : oldEvent.getRequestModeration())
                .title(dto.getTitle() != null ? dto.getTitle() : oldEvent.getTitle())
                .state(oldEvent.getState())
                .build();
    }

    public Event fromEventUpdateDto(EventUpdateDto dto) {
        Event event = eventValidation.eventIdValidation(dto.getEventId());
        return Event
                .builder()
                .id(dto.getEventId())
                .annotation(dto.getAnnotation())
                .category(dto.getCategory())
                .createdOn(event.getCreatedOn())
                .description(dto.getDescription())
                .eventDate(LocalDateTime.parse(dto.getEventDate(), FORMATTER))
                .initiator(event.getInitiator())
                .location(event.getLocation())
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .title(dto.getTitle())
                .state(event.getState())
                .build();
    }

    public EventShortDto toEventShortDto(Event event) {
        return EventShortDto
                .builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryService.findById(event.getCategory()))
                .eventDate(event.getEventDate().format(FORMATTER))
                .initiator(userService.findById(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(getViews(event.getId()))
                .confirmedRequests(requestService.findAllByEventAndStatus(event.getId(), RequestStatus.CONFIRMED).size())
                .build();
    }

    public EventFullDto toEventFullDto(Event event) {
        return EventFullDto
                .builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryService.findById(event.getCategory()))
                .createdOn(event.getCreatedOn().format(FORMATTER))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(FORMATTER))
                .initiator(userService.findById(event.getInitiator()))
                .location(locationService.findById(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn() == null ? null : event.getPublishedOn().format(FORMATTER))
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
                .state(event.getState())
                .views(getViews(event.getId()))
                .confirmedRequests(requestService.findAllByEventAndStatus(event.getId(), RequestStatus.CONFIRMED).size())
                .build();
    }

    private Integer getViews(Long eventId) {
        ResponseEntity<Object> response = statsClient.getStats(
                LocalDateTime.of(1970, 1, 1, 0, 0, 0).format(FORMATTER),
                LocalDateTime.now().format(FORMATTER),
                List.of("", "/events/" + eventId, ""),
                false);
        List<LinkedHashMap<String, Object>> stats = (List<LinkedHashMap<String, Object>>) response.getBody();
        return stats.size() == 0 ? 0 : (Integer) stats.get(0).get("hits");
    }
}
