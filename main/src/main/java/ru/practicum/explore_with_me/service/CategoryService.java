package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.dto.CategoryDto;
import ru.practicum.explore_with_me.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto add(NewCategoryDto newCategoryDto);

    CategoryDto update(CategoryDto categoryDto);

    void deleteById(Long id);

    List<CategoryDto> findAll(Integer from, Integer size);

    CategoryDto findById(Long catId);
}
