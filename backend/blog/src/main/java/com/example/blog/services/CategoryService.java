package com.example.blog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blog.DTOs.category.CategoryResponse;
import com.example.blog.entities.Category;
import com.example.blog.mappers.CategoryMapper;
import static com.example.blog.mappers.CategoryMapper.toDTO;
import com.example.blog.repositories.CategoryRepository;

@Service
// Service for managing blog categories
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryMapper::toDTO).toList();
    }

    public CategoryResponse getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name).orElseThrow(() -> new RuntimeException("Category not found!"));
        return toDTO(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public CategoryResponse createCategory(Category category) {
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new RuntimeException("Category already exists");
        }
        categoryRepository.save(category);
        return toDTO(category);
    }

    public CategoryResponse updateCategory(Long id, String newName) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        if (categoryRepository.findByName(newName).isPresent()) {
            throw new RuntimeException("Category with this name already exists");
        }
        if (newName != null && !newName.isEmpty()) {
            existingCategory.setName(newName);
        }
        categoryRepository.save(existingCategory);
        return toDTO(existingCategory);
    }

    public void deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        }

    }
}
