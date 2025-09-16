package com.example.blog.mappers;

import com.example.blog.DTOs.category.CategoryResponse;
import com.example.blog.entities.Category;

// Mapper for Category entity and DTO
public class CategoryMapper {
    public static CategoryResponse toDTO(Category category) {
        CategoryResponse categoryDTO = new CategoryResponse();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    public static Category toCategoryEntity(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }
}
