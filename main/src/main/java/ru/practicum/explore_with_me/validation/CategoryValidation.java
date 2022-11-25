package ru.practicum.explore_with_me.validation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.exception.NotFoundException;
import ru.practicum.explore_with_me.exception.ValidationException;
import ru.practicum.explore_with_me.model.Category;
import ru.practicum.explore_with_me.repository.CategoryRepository;
import ru.practicum.explore_with_me.repository.EventRepository;

import java.util.Optional;

@AllArgsConstructor
@Component
public class CategoryValidation {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public Category categoryIdValidation(Long catId) {
        Optional<Category> category = categoryRepository.findById(catId);
        if (category.isEmpty()) {
            throw new NotFoundException("Category not found");
        }
        return category.get();
    }

    public void categoryContainsEventsValidation(Long catId) {
        if (eventRepository.findAllByCategory(catId).size() > 0) {
            throw new ValidationException("Events is associated with category");
        }
    }
}
