package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.client.StatsClient;
import ru.practicum.explore_with_me.dto.CategoryDto;
import ru.practicum.explore_with_me.dto.CompilationDto;
import ru.practicum.explore_with_me.dto.EventFullDto;
import ru.practicum.explore_with_me.dto.EventShortDto;
import ru.practicum.explore_with_me.service.CategoryService;
import ru.practicum.explore_with_me.service.CompilationService;
import ru.practicum.explore_with_me.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "")
public class PublicController {

    private final CategoryService categoryService;
    private final CompilationService compilationService;
    private final EventService eventService;
    private final StatsClient statsClient;

    @GetMapping("/events")
    public List<EventShortDto> findAllEvents(@RequestParam String text,
                                             @RequestParam List<Long> categories,
                                             @RequestParam Boolean paid,
                                             @RequestParam(required = false) String rangeStart,
                                             @RequestParam(required = false) String rangeEnd,
                                             @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                             @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                             @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                             @Positive @RequestParam(defaultValue = "100") Integer size,
                                             HttpServletRequest request) {
        statsClient.addStats(request.getRemoteAddr(), request.getRequestURI());
        return eventService.findAll(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/events/{id}")
    public EventFullDto findEventById(@PathVariable Long id, HttpServletRequest request) {
        statsClient.addStats(request.getRemoteAddr(), request.getRequestURI());
        return eventService.findById(id);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> findAllCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                                    @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(defaultValue = "10") Integer size) {
        return compilationService.findAll(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto findCompilationById(@PathVariable Long compId) {
        return compilationService.findById(compId);
    }

    @GetMapping("/categories")
    public List<CategoryDto> findAllCategories(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                               @Positive @RequestParam(defaultValue = "100") Integer size) {
        return categoryService.findAll(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto findCategoryById(@PathVariable Long catId) {
        return categoryService.findById(catId);
    }
}
