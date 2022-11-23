package ru.practicum.explore_with_me.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.client.StatsClient;
import ru.practicum.explore_with_me.dto.*;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.EventState;
import ru.practicum.explore_with_me.model.RequestStatus;
import ru.practicum.explore_with_me.repository.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ParticipationRequestRepository participationRequestRepository;
    private final EventRepository eventRepository;
    private final StatsClient statsClient;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Event fromNewEventDto(NewEventDto newEventDto) {
        Long location = locationRepository.save(locationMapper.fromLocationDto(newEventDto.getLocation())).getId();
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
        Event oldEvent = eventRepository.findById(oldEvenId).get();
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
                .location(dto.getLocation() != null ? locationRepository.save(locationMapper
                        .fromLocationDto(dto.getLocation())).getId() : oldEvent.getLocation())
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
        Event event = eventRepository.findById(dto.getEventId()).get();
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
                .category(categoryMapper.toCategoryDto(categoryRepository.findById(event.getCategory()).get()))
                .eventDate(event.getEventDate().format(FORMATTER))
                .initiator(userMapper.toUserShortDto(userRepository.findById(event.getInitiator()).get()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(getViews(event.getId()))
                .confirmedRequests(participationRequestRepository
                        .findAllByEventAndStatus(event.getId(), RequestStatus.CONFIRMED).size())
                .build();
    }

    public EventFullDto toEventFullDto(Event event) {
        return EventFullDto
                .builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDto(categoryRepository.findById(event.getCategory()).get()))
                .createdOn(event.getCreatedOn().format(FORMATTER))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(FORMATTER))
                .initiator(userMapper.toUserShortDto(userRepository.findById(event.getInitiator()).get()))
                .location(locationMapper.toLocationDto(locationRepository.findById(event.getLocation()).get()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn() == null ? null : event.getPublishedOn().format(FORMATTER))
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
                .state(event.getState())
                .views(getViews(event.getId()))
                .confirmedRequests(participationRequestRepository
                        .findAllByEventAndStatus(event.getId(), RequestStatus.CONFIRMED).size())
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
