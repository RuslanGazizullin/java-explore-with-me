package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.EndpointHit;
import ru.practicum.explore_with_me.dto.ViewStats;
import ru.practicum.explore_with_me.mapper.HitsMapper;
import ru.practicum.explore_with_me.model.Hits;
import ru.practicum.explore_with_me.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final HitsMapper hitsMapper;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void add(EndpointHit endpointHit) {
        log.info("Statistics by uri {} saved", endpointHit.getUri());
        statsRepository.save(hitsMapper.fromEndpointHit(endpointHit));
    }

    @Override
    public List<ViewStats> findAll(String start, String end, List<String> uris, Boolean unique) {
        List<Hits> hits = statsRepository.findAll(
                LocalDateTime.parse(start, FORMATTER),
                LocalDateTime.parse(end, FORMATTER),
                uris == null ? Collections.emptyList() : uris);
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
