package ru.practicum.explore_with_me.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.EndpointHit;
import ru.practicum.explore_with_me.dto.ViewStats;
import ru.practicum.explore_with_me.mapper.HitsMapper;
import ru.practicum.explore_with_me.model.Hits;
import ru.practicum.explore_with_me.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping
@Slf4j
public class StatsController {

    private final StatsRepository statsRepository;
    private final HitsMapper hitsMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/hit")
    public void add(@RequestBody EndpointHit endpointHit) {
        log.info("Statistics by uri {} saved", endpointHit.getUri());
        statsRepository.save(hitsMapper.fromEndpointHit(endpointHit));
    }

    @GetMapping("/stats")
    public List<ViewStats> findAll(@RequestParam String start,
                                   @RequestParam String end,
                                   @RequestParam(defaultValue = "[]") List<String> uris,
                                   @RequestParam(defaultValue = "false") Boolean unique) {
        List<Hits> hits = statsRepository.findAll(LocalDateTime.parse(start, formatter),
                LocalDateTime.parse(end, formatter),
                uris);
        if (unique) {
            List<Hits> uniqueHits = new ArrayList<>();
            List<String> uniqueIps = hits.stream().map(Hits::getIp).distinct().collect(Collectors.toList());
            for (String ip : uniqueIps) {
                uniqueHits.add(hits.stream().filter(x -> x.getIp().equals(ip)).findFirst().get());
            }
            hits = uniqueHits;
        }
        log.info("Statistics received");
        return hits.stream().map(hitsMapper::toViewStats).collect(Collectors.toList());
    }
}
