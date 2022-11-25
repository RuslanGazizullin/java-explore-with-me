package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.LocationDto;
import ru.practicum.explore_with_me.mapper.LocationMapper;
import ru.practicum.explore_with_me.model.Location;
import ru.practicum.explore_with_me.repository.LocationRepository;

@RequiredArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public Location add(LocationDto locationDto) {
        Location location = locationMapper.fromLocationDto(locationDto);
        return locationRepository.save(location);
    }

    @Override
    public LocationDto findById(Long id) {
        Location location = locationRepository.findById(id).get();
        return locationMapper.toLocationDto(location);
    }
}
