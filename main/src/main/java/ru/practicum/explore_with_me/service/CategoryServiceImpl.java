package ru.practicum.explore_with_me.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.CategoryDto;
import ru.practicum.explore_with_me.dto.NewCategoryDto;
import ru.practicum.explore_with_me.mapper.CategoryMapper;
import ru.practicum.explore_with_me.model.Category;
import ru.practicum.explore_with_me.repository.CategoryRepository;
import ru.practicum.explore_with_me.validation.CategoryValidation;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryValidation categoryValidation;

    @Override
    public List<CategoryDto> findAll(Integer from, Integer size) {
        log.info("All categories found");
        return categoryRepository.findAll(PageRequest.of(from / size, size))
                .stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findById(Long catId) {
        Category category = categoryValidation.categoryIdValidation(catId);
        log.info("Category id {} found", catId);
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto add(NewCategoryDto newCategoryDto) {
        log.info("Category added");
        Category category = categoryMapper.fromNewCategoryDto(newCategoryDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryDto(savedCategory);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        Long catId = categoryDto.getId();
        categoryValidation.categoryIdValidation(catId);
        Category category = categoryMapper.fromCategoryDto(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        log.info("Category id {} updated", catId);
        return categoryMapper.toCategoryDto(savedCategory);
    }

    @Override
    public void deleteById(Long catId) {
        categoryValidation.categoryIdValidation(catId);
        categoryValidation.categoryContainsEventsValidation(catId);
        log.info("Category id {} deleted", catId);
        categoryRepository.deleteById(catId);
    }
}
