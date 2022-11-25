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
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<EventShortDto> findAll(String text, List<Long> categories, Boolean paid, String rangeStart,
                                       String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size) {
        List<EventShortDto> events;
        if (rangeStart != null && rangeEnd != null) {
            events = eventRepository.findAll(text, categories, paid, LocalDateTime.parse(rangeStart, FORMATTER),
                            LocalDateTime.parse(rangeEnd, FORMATTER), PageRequest.of(from / size, size))
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
            events = events.stream().sorted(Comparator.comparing(EventShortDto::getViews)).collect(Collectors.toList());
        }
        log.info("All events found");
        return events;
    }

    @Override
    public EventFullDto findById(Long id) {
        eventValidation.eventIdValidation(id);
        log.info("Event id {} found", id);
        return eventMapper.toEventFullDto(eventRepository.findByIdAndState(id, EventState.PUBLISHED));
    }

    @Override
    public List<EventShortDto> findAllByUser(Long userId, Integer from, Integer size) {
        log.info("All event by user id {} found", userId);
        return eventRepository.findAllByInitiator(PageRequest.of(from / size, size), userId)
                .stream()
                .filter(event -> event.getInitiator().equals(userId))
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateByUser(EventUpdateDto eventUpdateDto, Long userId) {
        Long eventId = eventUpdateDto.getEventId();
        eventValidation.eventForUpdateValidation(eventId, userId);
        Event event = eventMapper.fromEventUpdateDto(eventUpdateDto);
        if (event.getState().equals(EventState.CANCELED)) {
            event.setState(EventState.PENDING);
        }
        Event savedEvent = eventRepository.save(event);
        log.info("Event id {} updated by user id {}", eventId, userId);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    public EventFullDto addByUser(NewEventDto newEventDto, Long userId) {
        Event event = eventMapper.fromNewEventDto(newEventDto);
        eventValidation.eventDateValidation(event.getEventDate());
        event.setInitiator(userId);
        Event savedEvent = eventRepository.save(event);
        log.info("Event added by user id {}", userId);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    public EventFullDto findByIdByUser(Long userId, Long eventId) {
        Event event = eventValidation.eventIdAndInitiatorValidation(eventId, userId);
        log.info("Event id {} found by user id {}", eventId, userId);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto cancelByUser(Long userId, Long eventId) {
        Event event = eventValidation.eventIdAndInitiatorValidation(eventId, userId);
        if (event.getState().equals(EventState.PENDING)) {
            event.setState(EventState.CANCELED);
        }
        Event savedEvent = eventRepository.save(event);
        log.info("Event id {} canceled by user id {}", eventId, userId);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    public List<EventFullDto> findAllByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                             String rangeStart, String rangeEnd, Integer from, Integer size) {
        log.info("All events found");
        if (rangeStart != null && rangeEnd != null) {
            return eventRepository.findAllByAdmin(users, categories, LocalDateTime.parse(rangeStart, FORMATTER),
                            LocalDateTime.parse(rangeEnd, FORMATTER), PageRequest.of(from / size, size))
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
        Event event = eventMapper.fromEventUpdateAdminDto(eventUpdateAdminDto, eventId);
        Event savedEvent = eventRepository.save(event);
        log.info("Event id {} updated by admin", eventId);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    public EventFullDto publishByAdmin(Long eventId) {
        Event event = eventValidation.publishEventValidation(eventId);
        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventState.PUBLISHED);
        Event savedEvent = eventRepository.save(event);
        log.info("Event id {} published by admin", eventId);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    public EventFullDto rejectByAdmin(Long eventId) {
        Event event = eventValidation.rejectEventValidation(eventId);
        event.setState(EventState.CANCELED);
        Event savedEvent = eventRepository.save(event);
        log.info("Event id {} rejected by admin", eventId);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public Event findByIdForMapper(Long id) {
        return eventValidation.eventIdValidation(id);
    }
}
