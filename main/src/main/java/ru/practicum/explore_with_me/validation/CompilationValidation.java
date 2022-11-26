package ru.practicum.explore_with_me.validation;

import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.exception.NotFoundException;
import ru.practicum.explore_with_me.model.Compilation;
import ru.practicum.explore_with_me.repository.CompilationRepository;

import java.util.Optional;

@Component
public class CompilationValidation {

    private final CompilationRepository compilationRepository;

    public CompilationValidation(CompilationRepository compilationRepository) {
        this.compilationRepository = compilationRepository;
    }

    public Compilation compilationIdValidation(Long id) {
        Optional<Compilation> compilation = compilationRepository.findById(id);
        if (compilation.isEmpty()) {
            throw new NotFoundException("Compilation not found");
        }
        return compilation.get();
    }
}
