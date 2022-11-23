package ru.practicum.explore_with_me.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.CompilationDto;
import ru.practicum.explore_with_me.dto.NewCompilationDto;
import ru.practicum.explore_with_me.service.CompilationService;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {

    private final CompilationService compilationService;

    public AdminCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    public CompilationDto add(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return compilationService.add(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteById(@PathVariable Long compId) {
        compilationService.deleteById(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpin(@PathVariable Long compId) {
        compilationService.unpin(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void pin(@PathVariable Long compId) {
        compilationService.pin(compId);
    }
}
