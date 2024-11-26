package com.interverse.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interverse.demo.model.Product;
import com.interverse.demo.model.ProductRepository;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepo;
	
	
	public Product saveProduct(Product product) {
		return productRepo.save(product);
		
		
	}
	
	
	public Product findProductById(Integer id) {
		
		Optional<Product> optional = productRepo.findById(id);
		
		if (optional.isPresent()) {
			Product result = optional.get();
			return result;
		}
		
		return null;
	}
	

    public List<Product> findAllProducts() {
        return productRepo.findAll();
    }

    
    public Product updateProduct(Integer id, Product productDetails) {
        Product product = findProductById(id);
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setColor(productDetails.getColor());
        // 更新其他屬性...
        return productRepo.save(product);
    }

    public void deleteProduct(Integer id) {
        Product product = findProductById(id);
        productRepo.delete(product);
    }
    
    
    
	
}
