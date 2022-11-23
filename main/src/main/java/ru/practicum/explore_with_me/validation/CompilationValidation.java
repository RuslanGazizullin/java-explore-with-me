package ru.practicum.explore_with_me.validation;

import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.exception.NotFoundException;
import ru.practicum.explore_with_me.repository.CompilationRepository;

@Component
public class CompilationValidation {

    private final CompilationRepository compilationRepository;

    public CompilationValidation(CompilationRepository compilationRepository) {
        this.compilationRepository = compilationRepository;
    }

    public void compilationIdValidation(Long id) {
        if (compilationRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Compilation not found");
        }
    }
}
