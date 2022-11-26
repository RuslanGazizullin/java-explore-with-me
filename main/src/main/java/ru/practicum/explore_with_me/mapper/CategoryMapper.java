package ru.practicum.explore_with_me.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.CategoryDto;
import ru.practicum.explore_with_me.dto.NewCategoryDto;
import ru.practicum.explore_with_me.model.Category;

@Component
public class CategoryMapper {
    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public Category fromCategoryDto(CategoryDto categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getName());
    }

    public Category fromNewCategoryDto(NewCategoryDto newCategoryDto) {
        return new Category(null, newCategoryDto.getName());
    }
}
