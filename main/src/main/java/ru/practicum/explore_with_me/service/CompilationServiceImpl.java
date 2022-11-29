package ru.practicum.explore_with_me.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.CompilationDto;
import ru.practicum.explore_with_me.dto.NewCompilationDto;
import ru.practicum.explore_with_me.mapper.CompilationMapper;
import ru.practicum.explore_with_me.model.Compilation;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.repository.CompilationRepository;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.validation.CompilationValidation;
import ru.practicum.explore_with_me.validation.EventValidation;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final CompilationValidation compilationValidation;
    private final EventValidation eventValidation;
    private final EventRepository eventRepository;

    @Override
    public List<CompilationDto> findAll(Boolean pinned, Integer from, Integer size) {
        log.info("All compilations found, pinned = {}", pinned);
        return compilationRepository.findAllByPinned(PageRequest.of(from / size, size), pinned)
                .stream()
                .map(compilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto findById(Long compId) {
        Compilation compilation = compilationValidation.compilationIdValidation(compId);
        log.info("Compilation id {} found", compId);
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    public CompilationDto add(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationMapper.fromNewCompilationDto(newCompilationDto);
        compilation.setEvents(eventRepository.findAll()
                .stream()
                .filter(event -> newCompilationDto.getEvents().contains(event.getId()))
                .collect(Collectors.toList()));
        Compilation savedCompilation = compilationRepository.save(compilation);
        log.info("Compilation added");
        return compilationMapper.toCompilationDto(savedCompilation);
    }

    @Override
    public void deleteById(Long compId) {
        compilationValidation.compilationIdValidation(compId);
        log.info("Compilation id {} deleted", compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        Event event = eventValidation.eventIdValidation(eventId);
        Compilation compilation = compilationValidation.compilationIdValidation(compId);
        compilation.getEvents().remove(event);
        compilationRepository.save(compilation);
        log.info("Event id {} deleted from compilation id {}", eventId, compId);
    }

    @Override
    public void addEventToCompilation(Long compId, Long eventId) {
        Event event = eventValidation.eventIdValidation(eventId);
        Compilation compilation = compilationValidation.compilationIdValidation(compId);
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
        log.info("Event id {} added to compilation id {}", eventId, compId);
    }

    @Override
    public void pin(Long compId) {
        Compilation compilation = compilationValidation.compilationIdValidation(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        log.info("Compilation id {} pinned", compId);
    }

    @Override
    public void unpin(Long compId) {
        Compilation compilation = compilationValidation.compilationIdValidation(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        log.info("Compilation id {} unpinned", compId);
    }
}
