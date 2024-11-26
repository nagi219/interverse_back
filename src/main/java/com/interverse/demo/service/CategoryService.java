package com.interverse.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.interverse.demo.model.Category;
import com.interverse.demo.model.CategoryRepository;



@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoriesRepo;
	
	
	public List<Category> findAllCategories() {
        return categoriesRepo.findAll();
    }
	
	
	public Category saveCategory(Category category) {
		
		return categoriesRepo.save(category);
		
	}
	
	public Category findCateGoryById(Integer idInteger) {
		Optional<Category> optional = categoriesRepo.findById(idInteger);
		
		if(optional.isPresent()) {
			Category result = optional.get();
			return result;
		}
		
		return null;
	}
	
	
	@Transactional
    public Category updateCategory(Category category) {
        if (category.getId() == null) {
            return null; // Cannot update a category without an ID
        }
        
        Optional<Category> optionalExisting = categoriesRepo.findById(category.getId());
        if (optionalExisting.isPresent()) {
            Category existingCategory = optionalExisting.get();
            existingCategory.setName(category.getName());
            // Update other fields as necessary
            return categoriesRepo.save(existingCategory);
        }
        return null; // Category not found
    }

    @Transactional
    public boolean deleteCategory(Integer categoryId) {
        if (categoriesRepo.existsById(categoryId)) {
            categoriesRepo.deleteById(categoryId);
            return true;
        }
        return false; // Category not found
    }
    
    
	
	
}
