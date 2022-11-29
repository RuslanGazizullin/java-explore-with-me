package ru.practicum.explore_with_me.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.EventFullDto;
import ru.practicum.explore_with_me.dto.EventUpdateDto;
import ru.practicum.explore_with_me.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/admin/events")
public class AdminEventController {

    private final EventService eventService;

    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventFullDto> findAll(@RequestParam(defaultValue = "[]") List<Long> users,
                                      @RequestParam(defaultValue = "[]") List<String> states,
                                      @RequestParam(defaultValue = "[]") List<Long> categories,
                                      @RequestParam(required = false) String rangeStart,
                                      @RequestParam(required = false) String rangeEnd,
                                      @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                      @Positive @RequestParam(defaultValue = "100") Integer size) {
        return eventService.findAllByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public EventFullDto update(@RequestBody EventUpdateDto eventUpdateDto, @PathVariable Long eventId) {
        return eventService.updateByAdmin(eventId, eventUpdateDto);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publish(@PathVariable Long eventId) {
        return eventService.publishByAdmin(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto reject(@PathVariable Long eventId) {
        return eventService.rejectByAdmin(eventId);
    }
}
