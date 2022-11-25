package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.dto.LocationDto;
import ru.practicum.explore_with_me.model.Location;

public interface LocationService {
    Location add(LocationDto locationDto);

    LocationDto findById(Long id);
}
