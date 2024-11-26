package com.interverse.demo.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interverse.demo.model.Product;
import com.interverse.demo.model.ProductPhotos;
import com.interverse.demo.service.ProductPhotoService;
import com.interverse.demo.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductPhotoService productPhotoService;
	
	
	@GetMapping("/{productId}/firstphoto")
    public ResponseEntity<Resource> getFirstProductPhoto(@PathVariable Integer productId) throws MalformedURLException {
        List<ProductPhotos> photos = productPhotoService.getAllProductPhotos(productId);
        
        if (photos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        ProductPhotos firstPhoto = photos.get(0);
        String photoPath = firstPhoto.getPhotoPath(); 
        
        Path path = Paths.get(photoPath);
        Resource resource = new UrlResource(path.toUri());
        
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.IMAGE_JPEG) // 或者根據實際情況設置
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	
	@GetMapping("/{productId}/latestphoto")
    public ResponseEntity<Resource> getLatestProductPhoto(@PathVariable Integer productId) throws MalformedURLException {
        List<ProductPhotos> photos = productPhotoService.getAllProductPhotos(productId);

        if (photos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ProductPhotos latestPhoto = photos.stream()
                .max(Comparator.comparing(ProductPhotos::getAdded))
                .orElse(null);

        if (latestPhoto == null) {
            return ResponseEntity.notFound().build();
        }

        String photoPath = latestPhoto.getPhotoPath();

        Path path = Paths.get(photoPath);
        Resource resource = new UrlResource(path.toUri());

        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + latestPhoto.getPhotoName() + "\"")
                .contentType(MediaType.IMAGE_JPEG) // 或者根據實際情況設置
                .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

	
	@PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Product product = productService.findProductById(id);
        return ResponseEntity.ok(product);
    }
	
	@GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAllProducts();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{productId}/{photoId}")
    public ResponseEntity<Resource> getSpecificProductPhoto(@PathVariable Integer productId, @PathVariable Integer photoId) throws MalformedURLException {
        // 使用 productId 和 photoId 來獲取特定的照片
        ProductPhotos photo = productPhotoService.getProductPhoto(productId, photoId);

        if (photo == null) {
            return ResponseEntity.notFound().build();
        }

        String photoPath = photo.getPhotoPath();

        Path path = Paths.get(photoPath);
        Resource resource = new UrlResource(path.toUri());

        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + photo.getPhotoName() + "\"")
                .contentType(MediaType.IMAGE_JPEG) // 或者根據實際情況設置
                .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
}
