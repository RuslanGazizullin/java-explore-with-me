package ru.practicum.explore_with_me.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.LocationDto;
import ru.practicum.explore_with_me.model.Location;

@Component
public class LocationMapper {

    public LocationDto toLocationDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }

    public Location fromLocationDto(LocationDto locationDto) {
        return new Location(null, locationDto.getLat(), locationDto.getLon());
    }
}
