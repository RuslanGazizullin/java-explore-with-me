package ru.practicum.explore_with_me.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.EndpointHit;
import ru.practicum.explore_with_me.dto.ViewStats;
import ru.practicum.explore_with_me.service.StatsService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public void add(@RequestBody EndpointHit endpointHit) {
        statsService.add(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> findAll(@RequestParam String start,
                                   @RequestParam String end,
                                   @RequestParam List<String> uris,
                                   @RequestParam(defaultValue = "false") Boolean unique) {
        return statsService.findAll(start, end, uris, unique);
    }
}
