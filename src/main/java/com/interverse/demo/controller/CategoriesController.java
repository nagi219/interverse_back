package com.interverse.demo.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interverse.demo.model.Category;
import com.interverse.demo.service.CategoryService;


@RestController
@RequestMapping("/categories")
public class CategoriesController {
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/{categoryId}")
	public ResponseEntity<?> findCategory(@PathVariable("categoryId") Integer categoryId) {
		try {
			Category category = categoryService.findCateGoryById(categoryId);
			if (category != null) {
				return ResponseEntity.ok(category);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing your request");
		}
	}

	@GetMapping
	public List<Category> getAllCategories() {
		return categoryService.findAllCategories();
	}

	@PostMapping
	public ResponseEntity<Category> postCategories(@RequestBody Category category) {

		Category saveCategory = categoryService.saveCategory(category);
		return ResponseEntity.created(URI.create("/categories" + saveCategory.getId())).body(saveCategory);

	}

	@PutMapping("/{categoryId}")
	public ResponseEntity<Category> updateCategory(@PathVariable("categoryId") Integer categoryId,
			@RequestBody Category category) {
		category.setId(categoryId);
		Category updatedCategory = categoryService.updateCategory(category);
		if (updatedCategory != null) {
			return ResponseEntity.ok(updatedCategory);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{categoryId}")
	public ResponseEntity<Void> deleteCategory(@PathVariable("categoryId") Integer categoryId) {
		boolean deleted = categoryService.deleteCategory(categoryId);
		if (deleted) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
