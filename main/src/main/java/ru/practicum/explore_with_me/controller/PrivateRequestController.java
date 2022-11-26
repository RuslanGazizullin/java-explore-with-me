package ru.practicum.explore_with_me.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.ParticipationRequestDto;
import ru.practicum.explore_with_me.service.ParticipationRequestService;

import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/users/{userId}/requests")
public class PrivateRequestController {

    private final ParticipationRequestService participationRequestService;

    public PrivateRequestController(ParticipationRequestService participationRequestService) {
        this.participationRequestService = participationRequestService;
    }

    @GetMapping
    public List<ParticipationRequestDto> findAll(@PathVariable Long userId) {
        return participationRequestService.findAll(userId);
    }

    @PostMapping
    public ParticipationRequestDto add(@PathVariable Long userId, @RequestParam Long eventId) {
        return participationRequestService.add(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        return participationRequestService.cancel(userId, requestId);
    }
}
