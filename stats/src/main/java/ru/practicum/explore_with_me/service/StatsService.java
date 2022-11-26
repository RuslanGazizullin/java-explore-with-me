package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.dto.EndpointHit;
import ru.practicum.explore_with_me.dto.ViewStats;

import java.util.List;

public interface StatsService {

    void add(EndpointHit endpointHit);

    List<ViewStats> findAll(String start, String end, List<String> uris, Boolean unique);
}
