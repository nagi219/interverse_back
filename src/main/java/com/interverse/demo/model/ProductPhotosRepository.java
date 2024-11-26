package com.interverse.demo.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductPhotosRepository extends JpaRepository<ProductPhotos, Integer> {

	 List<ProductPhotos> findByProducts(Product product);
	    
	    @Query("SELECT pp FROM ProductPhotos pp WHERE pp.products.id = :productId")
	    List<ProductPhotos> findByProductId(@Param("productId") Integer productId);
	    
	    @Query("SELECT pp FROM ProductPhotos pp WHERE pp.products.id = :productId AND pp.id = :photoId")
	    Optional<ProductPhotos> findByProductIdAndPhotoId(@Param("productId") Integer productId, @Param("photoId") Integer photoId);

	    // 新增的方法
	    Optional<ProductPhotos> findByProductsAndId(Product product, Integer id);
}
