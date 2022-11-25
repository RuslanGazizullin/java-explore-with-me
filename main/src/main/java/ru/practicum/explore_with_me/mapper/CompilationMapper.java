package ru.practicum.explore_with_me.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.CompilationDto;
import ru.practicum.explore_with_me.dto.NewCompilationDto;
import ru.practicum.explore_with_me.model.Compilation;
import ru.practicum.explore_with_me.service.EventService;

import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class CompilationMapper {

    private final EventService eventService;
    private final EventMapper eventMapper;

    public CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto
                .builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(eventService.findAll()
                        .stream()
                        .filter(event -> compilation.getEvents().contains(event))
                        .map(eventMapper::toEventShortDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public Compilation fromNewCompilationDto(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .events(eventService.findAll()
                        .stream()
                        .filter(event -> newCompilationDto.getEvents().contains(event.getId()))
                        .collect(Collectors.toList()))
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }
}
