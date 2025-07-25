package com.example.blog.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blog.entities.Category;
import com.example.blog.repositories.CategoryRepository;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        if (category.getName() == null || category.getName().isEmpty()) {
            throw new RuntimeException("No category specified!");
        }
        return categoryRepository.save(category);
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategory(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category updateCategory(Long id, Category category) {
        Category updatedcategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("category not found!"));
        if (category.getName() != null && !category.getName().isEmpty()) {
            updatedcategory.setName(category.getName());
        }
        return categoryRepository.save(updatedcategory);
    }

    public boolean deleteCategory(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
