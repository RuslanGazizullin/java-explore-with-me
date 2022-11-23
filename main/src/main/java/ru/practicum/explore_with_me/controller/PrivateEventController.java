package ru.practicum.explore_with_me.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.*;
import ru.practicum.explore_with_me.service.EventService;
import ru.practicum.explore_with_me.service.ParticipationRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping(path = "/users/{userId}/events")
public class PrivateEventController {

    private final EventService eventService;
    private final ParticipationRequestService participationRequestService;

    @GetMapping
    public List<EventShortDto> findAll(@PathVariable Long userId,
                                       @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                       @Positive @RequestParam(defaultValue = "100") Integer size) {
        return eventService.findAllByUser(userId, from, size);
    }

    @PatchMapping
    public EventFullDto update(@PathVariable Long userId, @RequestBody @Valid EventUpdateDto eventUpdateDto) {
        return eventService.updateByUser(eventUpdateDto, userId);
    }

    @PostMapping
    public EventFullDto add(@PathVariable Long userId, @RequestBody @Valid NewEventDto newEventDto) {
        return eventService.addByUser(newEventDto, userId);
    }

    @GetMapping("/{eventId}")
    public EventFullDto findById(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.findByIdByUser(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancel(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.cancelByUser(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findAllRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return participationRequestService.findAllByUserByEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable Long userId,
                                                  @PathVariable Long eventId,
                                                  @PathVariable Long reqId) {
        return participationRequestService.confirmByUser(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable Long userId,
                                                 @PathVariable Long eventId,
                                                 @PathVariable Long reqId) {
        return participationRequestService.rejectByUser(userId, eventId, reqId);
    }
}