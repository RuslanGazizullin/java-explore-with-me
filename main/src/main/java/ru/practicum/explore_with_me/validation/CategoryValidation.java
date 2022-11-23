package ru.practicum.explore_with_me.validation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.exception.NotFoundException;
import ru.practicum.explore_with_me.exception.ValidationException;
import ru.practicum.explore_with_me.repository.CategoryRepository;
import ru.practicum.explore_with_me.repository.EventRepository;

@AllArgsConstructor
@Component
public class CategoryValidation {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public void categoryIdValidation(Long catId) {
        if (categoryRepository.findById(catId).isEmpty()) {
            throw new NotFoundException("Category not found");
        }
    }

    public void categoryContainsEventsValidation(Long catId) {
        if (eventRepository.findAllByCategory(catId).size() > 0) {
            throw new ValidationException("Events is associated with category");
        }
    }
}
