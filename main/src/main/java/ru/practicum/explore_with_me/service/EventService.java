package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.dto.*;

import java.util.List;

public interface EventService {
    List<EventShortDto> findAll(String text, List<Long> categories, Boolean paid, String rangeStart,
                                String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size);

    EventFullDto findById(Long id);

    List<EventShortDto> findAllByUser(Long userId, Integer from, Integer size);

    EventFullDto updateByUser(EventUpdateDto eventUpdateDto, Long userId);

    EventFullDto addByUser(NewEventDto newEventDto, Long userId);

    EventFullDto findByIdByUser(Long userId, Long eventId);

    EventFullDto cancelByUser(Long userId, Long eventId);

    List<EventFullDto> findAllByAdmin(List<Long> users, List<String> state, List<Long> categories, String rangeStart,
                                  String rangeEnd, Integer from, Integer size);

    EventFullDto updateByAdmin(Long eventId, EventUpdateAdminDto eventUpdateAdminDto);

    EventFullDto publishByAdmin(Long eventId);

    EventFullDto rejectByAdmin(Long eventId);
}
