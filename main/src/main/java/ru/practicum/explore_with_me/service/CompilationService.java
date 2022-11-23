package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.dto.CompilationDto;
import ru.practicum.explore_with_me.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> findAll(Boolean pinned, Integer from, Integer size);

    CompilationDto findById(Long compId);

    CompilationDto add(NewCompilationDto newCompilationDto);

    void deleteById(Long compId);

    void deleteEventFromCompilation(Long compId, Long eventId);

    void addEventToCompilation(Long compId, Long eventId);

    void pin(Long compId);

    void unpin(Long compId);
}
