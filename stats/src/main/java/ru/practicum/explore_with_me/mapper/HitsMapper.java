package ru.practicum.explore_with_me.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.EndpointHit;
import ru.practicum.explore_with_me.dto.ViewStats;
import ru.practicum.explore_with_me.model.Hits;
import ru.practicum.explore_with_me.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class HitsMapper {

    private final StatsRepository statsRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HitsMapper(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    public ViewStats toViewStats(Hits hits) {
        return ViewStats
                .builder()
                .app(hits.getApp())
                .uri(hits.getUri())
                .hits(statsRepository.findAllByAppAndUri(hits.getApp(), hits.getUri()).size())
                .build();
    }

    public Hits fromEndpointHit(EndpointHit endpointHit) {
        return Hits
                .builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(LocalDateTime.parse(endpointHit.getTimestamp(), formatter))
                .build();
    }
}
