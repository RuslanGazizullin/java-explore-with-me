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
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;
    private final CompilationValidation compilationValidation;
    private final EventValidation eventValidation;

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
        compilationValidation.compilationIdValidation(compId);
        log.info("Compilation id {} found", compId);
        return compilationMapper.toCompilationDto(compilationRepository.findById(compId).get());
    }

    @Override
    public CompilationDto add(NewCompilationDto newCompilationDto) {
        log.info("Compilation added");
        return compilationMapper.toCompilationDto(compilationRepository.save(compilationMapper
                .fromNewCompilationDto(newCompilationDto)));
    }

    @Override
    public void deleteById(Long compId) {
        compilationValidation.compilationIdValidation(compId);
        log.info("Compilation id {} deleted", compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        eventValidation.eventIdValidation(eventId);
        compilationValidation.compilationIdValidation(compId);
        Event event = eventRepository.findById(eventId).get();
        Compilation compilation = compilationRepository.findById(compId).get();
        compilation.getEvents().remove(event);
        compilationRepository.save(compilation);
        log.info("Event id {} deleted from compilation id {}", eventId, compId);
    }

    @Override
    public void addEventToCompilation(Long compId, Long eventId) {
        eventValidation.eventIdValidation(eventId);
        compilationValidation.compilationIdValidation(compId);
        Event event = eventRepository.findById(eventId).get();
        Compilation compilation = compilationRepository.findById(compId).get();
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
        log.info("Event id {} added to compilation id {}", eventId, compId);
    }

    @Override
    public void pin(Long compId) {
        compilationValidation.compilationIdValidation(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        log.info("Compilation id {} pinned", compId);
    }

    @Override
    public void unpin(Long compId) {
        compilationValidation.compilationIdValidation(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        log.info("Compilation id {} unpinned", compId);
    }
}
