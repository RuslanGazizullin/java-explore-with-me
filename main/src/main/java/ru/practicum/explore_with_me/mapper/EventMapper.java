package ru.practicum.explore_with_me.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.client.StatsClient;
import ru.practicum.explore_with_me.dto.*;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.EventState;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final LocationMapper locationMapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final StatsClient statsClient;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Event fromNewEventDto(NewEventDto newEventDto) {
        return Event
                .builder()
                .annotation(newEventDto.getAnnotation())
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER))
                .location(locationMapper.fromLocationDto(newEventDto.getLocation()))
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .state(EventState.PENDING)
                .build();
    }

    public Event fromEventUpdateDto(EventUpdateDto dto, Event oldEvent) {
        return Event
                .builder()
                .id(dto.getEventId() == null ? oldEvent.getId() : dto.getEventId())
                .annotation(dto.getAnnotation() == null ? oldEvent.getAnnotation() : dto.getAnnotation())
                .createdOn(oldEvent.getCreatedOn())
                .description(dto.getDescription() == null ? oldEvent.getDescription() : dto.getDescription())
                .eventDate(dto.getEventDate() == null ? oldEvent.getEventDate()
                        : LocalDateTime.parse(dto.getEventDate(), FORMATTER))
                .initiator(oldEvent.getInitiator())
                .location(dto.getLocation() == null ? oldEvent.getLocation()
                        : locationMapper.fromLocationDto(dto.getLocation()))
                .paid(dto.getPaid() == null ? oldEvent.getPaid() : dto.getPaid())
                .participantLimit(dto.getParticipantLimit() == null ? oldEvent.getParticipantLimit()
                        : dto.getParticipantLimit())
                .publishedOn(oldEvent.getPublishedOn())
                .requestModeration(dto.getRequestModeration() == null ? oldEvent.getRequestModeration()
                        : dto.getRequestModeration())
                .title(dto.getTitle() == null ? oldEvent.getTitle() : dto.getTitle())
                .state(oldEvent.getState())
                .build();
    }

    public EventShortDto toEventShortDto(Event event) {
        return EventShortDto
                .builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate().format(FORMATTER))
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(getViews(event.getId()))
                .build();
    }

    public EventFullDto toEventFullDto(Event event) {
        return EventFullDto
                .builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedOn().format(FORMATTER))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(FORMATTER))
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .location(locationMapper.toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn() == null ? null : event.getPublishedOn().format(FORMATTER))
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
                .state(event.getState())
                .views(getViews(event.getId()))
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
