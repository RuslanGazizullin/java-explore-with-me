package ru.practicum.explore_with_me.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.CategoryDto;
import ru.practicum.explore_with_me.dto.NewCategoryDto;
import ru.practicum.explore_with_me.mapper.CategoryMapper;
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
        categoryValidation.categoryIdValidation(catId);
        log.info("Category id {} found", catId);
        return categoryMapper.toCategoryDto(categoryRepository.findById(catId).get());
    }

    @Override
    public CategoryDto add(NewCategoryDto newCategoryDto) {
        log.info("Category added");
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.fromNewCategoryDto(newCategoryDto)));
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        categoryValidation.categoryIdValidation(categoryDto.getId());
        log.info("Category id {} updated", categoryDto.getId());
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.fromCategoryDto(categoryDto)));
    }

    @Override
    public void deleteById(Long catId) {
        categoryValidation.categoryIdValidation(catId);
        categoryValidation.categoryContainsEventsValidation(catId);
        log.info("Category id {} deleted", catId);
        categoryRepository.deleteById(catId);
    }
}
