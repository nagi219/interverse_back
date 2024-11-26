package com.interverse.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.interverse.demo.model.ProductPhotos;
import com.interverse.demo.service.ProductPhotoService;


@RestController
@RequestMapping("/api/product-photos")
public class ProductPhotosContoller {
	
	@Autowired
	private ProductPhotoService productPhotoService;
	
	 @PostMapping
	    public ResponseEntity<ProductPhotos> createProductPhoto(
	            @RequestParam("file") MultipartFile file,
	            @RequestParam("productId") Integer productId) throws IOException {
	        ProductPhotos createdPhoto = productPhotoService.createProductPhotos(file, productId);
	        return new ResponseEntity<>(createdPhoto, HttpStatus.CREATED);
	    }

	   @PutMapping("/{photoId}")
	   public ResponseEntity<ProductPhotos> updateProductPhoto(
	            @PathVariable Integer photoId,
	            @RequestParam("file") MultipartFile file) throws IOException {
	        ProductPhotos updatedPhoto = productPhotoService.updateProductPhoto(photoId, file);
	        return ResponseEntity.ok(updatedPhoto);
	    }

	    @GetMapping("/{photoId}")
	    public ResponseEntity<ProductPhotos> getProductPhoto(@PathVariable Integer photoId) {
	        ProductPhotos photo = productPhotoService.getProductPhoto(photoId);
	        return ResponseEntity.ok(photo);
	    }

	    @GetMapping("/product/{productId}")
	    public ResponseEntity<List<ProductPhotos>> getAllProductPhotos(@PathVariable Integer productId) {
	        List<ProductPhotos> photos = productPhotoService.getAllProductPhotos(productId);
	        return ResponseEntity.ok(photos);
	    }

	    @DeleteMapping("/{photoId}")
	    public ResponseEntity<Void> deleteProductPhoto(@PathVariable Integer photoId) throws IOException {
	        productPhotoService.deleteProductPhoto(photoId);
	        return ResponseEntity.noContent().build();
	    }

	    @ExceptionHandler(IOException.class)
	    public ResponseEntity<String> handleIOException(IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while processing the file: " + e.getMessage());
	    }
	

}
