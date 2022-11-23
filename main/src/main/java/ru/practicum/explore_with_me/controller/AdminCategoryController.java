package ru.practicum.explore_with_me.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.CategoryDto;
import ru.practicum.explore_with_me.dto.NewCategoryDto;
import ru.practicum.explore_with_me.service.CategoryService;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public CategoryDto add(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoryService.add(newCategoryDto);
    }

    @PatchMapping
    public CategoryDto update(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.update(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteById(@PathVariable Long catId) {
        categoryService.deleteById(catId);
    }
}

