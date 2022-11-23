package ru.practicum.explore_with_me.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.*;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.EventState;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.validation.EventValidation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventValidation eventValidation;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<EventShortDto> findAll(String text, List<Long> categories, Boolean paid, String rangeStart,
                                       String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size) {
        List<EventShortDto> events;
        if (rangeStart != null && rangeEnd != null) {
            events = eventRepository.findAll(text, categories, paid, LocalDateTime.parse(rangeStart, formatter),
                            LocalDateTime.parse(rangeEnd, formatter), PageRequest.of(from / size, size))
                    .stream()
                    .filter(event -> event.getState().equals(EventState.PUBLISHED))
                    .map(eventMapper::toEventShortDto)
                    .collect(Collectors.toList());
        } else {
            events = eventRepository.findAll(text, categories, paid, LocalDateTime.now(),
                            PageRequest.of(from / size, size))
                    .stream()
                    .filter(event -> event.getState().equals(EventState.PUBLISHED))
                    .map(eventMapper::toEventShortDto)
                    .collect(Collectors.toList());
        }
        if (sort.equals("VIEWS")) {
            events.stream().sorted(Comparator.comparing(EventShortDto::getViews)).collect(Collectors.toList());
        }
        return events;
    }

    @Override
    public EventFullDto findById(Long id) {
        eventValidation.eventIdValidation(id);
        return eventMapper.toEventFullDto(eventRepository.findByIdAndState(id, EventState.PUBLISHED));
    }

    @Override
    public List<EventShortDto> findAllByUser(Long userId, Integer from, Integer size) {
        return eventRepository.findAllByInitiator(PageRequest.of(from / size, size), userId)
                .stream()
                .filter(event -> event.getInitiator().equals(userId))
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateByUser(EventUpdateDto eventUpdateDto, Long userId) {
        eventValidation.eventForUpdateValidation(eventUpdateDto.getEventId(), userId);
        Event event = eventMapper.fromEventUpdateDto(eventUpdateDto);
        eventValidation.eventDateValidation(event.getEventDate());
        if (eventRepository.findById(eventUpdateDto.getEventId()).get().getState().equals(EventState.CANCELED)) {
            event.setState(EventState.PENDING);
        }
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto addByUser(NewEventDto newEventDto, Long userId) {
        Event event = eventMapper.fromNewEventDto(newEventDto);
        eventValidation.eventDateValidation(event.getEventDate());
        event.setInitiator(userId);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto findByIdByUser(Long userId, Long eventId) {
        eventValidation.eventIdValidation(eventId);
        return eventMapper.toEventFullDto(eventRepository.findByIdAndInitiator(eventId, userId));
    }

    @Override
    public EventFullDto cancelByUser(Long userId, Long eventId) {
        eventValidation.eventIdValidation(eventId);
        Event event = eventRepository.findByIdAndInitiator(eventId, userId);
        if (event.getState().equals(EventState.PENDING)) {
            event.setState(EventState.CANCELED);
        }
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> findAllByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                             String rangeStart, String rangeEnd, Integer from, Integer size) {
        if (rangeStart != null && rangeEnd != null) {
            return eventRepository.findAllByAdmin(users, categories, LocalDateTime.parse(rangeStart, formatter),
                            LocalDateTime.parse(rangeEnd, formatter), PageRequest.of(from / size, size))
                    .stream()
                    .filter(event -> states.contains(event.getState().name()))
                    .map(eventMapper::toEventFullDto)
                    .collect(Collectors.toList());
        } else {
            return eventRepository.findAllByAdmin(users, categories, LocalDateTime.now(),
                            PageRequest.of(from / size, size))
                    .stream()
                    .filter(event -> states.contains(event.getState().name()))
                    .map(eventMapper::toEventFullDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public EventFullDto updateByAdmin(Long eventId, EventUpdateAdminDto eventUpdateAdminDto) {
        return eventMapper.toEventFullDto(eventRepository.save(eventMapper
                .fromEventUpdateAdminDto(eventUpdateAdminDto, eventId)));
    }

    @Override
    public EventFullDto publishByAdmin(Long eventId) {
        eventValidation.publishEventValidation(eventId);
        Event event = eventRepository.findById(eventId).get();
        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventState.PUBLISHED);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto rejectByAdmin(Long eventId) {
        eventValidation.rejectEventValidation(eventId);
        Event event = eventRepository.findById(eventId).get();
        event.setState(EventState.CANCELED);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    private Event updateEvent(Event updatedEvent, Event oldEvent) {
        return Event.builder()
                .annotation(updatedEvent.getAnnotation() == null ? oldEvent.getAnnotation() : updatedEvent.getAnnotation())
                .category(updatedEvent.getCategory() == null ? oldEvent.getCategory() : updatedEvent.getCategory())
                .createdOn(updatedEvent.getCreatedOn() == null ? oldEvent.getCreatedOn() : updatedEvent.getCreatedOn())
                .description(updatedEvent.getDescription() == null ? oldEvent.getDescription()
                        : updatedEvent.getDescription())
                .eventDate(updatedEvent.getEventDate() == null ? oldEvent.getEventDate() : updatedEvent.getEventDate())
                .initiator(updatedEvent.getInitiator() == null ? oldEvent.getInitiator() : updatedEvent.getInitiator())
                .location(updatedEvent.getLocation() == null ? oldEvent.getLocation() : updatedEvent.getLocation())
                .paid(updatedEvent.getPaid() == null ? oldEvent.getPaid() : updatedEvent.getPaid())
                .participantLimit(updatedEvent.getParticipantLimit() == null ? oldEvent.getParticipantLimit()
                        : updatedEvent.getParticipantLimit())
                .publishedOn(updatedEvent.getPublishedOn() == null ? oldEvent.getPublishedOn()
                        : updatedEvent.getPublishedOn())
                .requestModeration(updatedEvent.getRequestModeration() == null ? oldEvent.getRequestModeration()
                        : updatedEvent.getRequestModeration())
                .title(updatedEvent.getTitle() == null ? oldEvent.getTitle() : updatedEvent.getTitle())
                .state(updatedEvent.getState() == null ? oldEvent.getState() : updatedEvent.getState())
                .build();
    }
}
