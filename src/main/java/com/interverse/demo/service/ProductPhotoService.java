package com.interverse.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.interverse.demo.model.Product;
import com.interverse.demo.model.ProductPhotos;
import com.interverse.demo.model.ProductPhotosRepository;
import com.interverse.demo.model.ProductRepository;

@Service
public class ProductPhotoService {
	
	@Value("${upload.product.dir}")
	private String uploadDir;
	
	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private ProductPhotosRepository productPhotosRepo;
	
	@Transactional
	public ProductPhotos createProductPhotos(MultipartFile file,Integer id) throws IOException {
		//取代option直接傳出product出來 要塞進去class內 ManyTOone
		Product product = productRepo.findById(id).orElseThrow(()-> new RuntimeException("ProductPhoto not found with id: " + id));
		

		//獲取上傳文件的名字  再去除路徑的不安全字符
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		//生成一個唯一的文件名 防止名字衝突 文件名前面加上uuid再加上原始文件名
		String uniqueFileName =  UUID.randomUUID().toString() + "_" + fileName;
		//將路徑轉變為Path類別的的物件
		Path uploaPath = Paths.get(uploadDir);
		
		//判斷目錄是否存在 不存在的話建立目錄
		if (!Files.exists(uploaPath)) {
			Files.createDirectories(uploaPath);
		}
		//生成完整的路徑ex:D:/product_photos/ +12345_image.jpg
		Path filePath = uploaPath.resolve(uniqueFileName);
		//將Path轉型為File符合參數 將檔案寫入指定地點
		file.transferTo(filePath.toFile());
		

		
		//存入class
		// 更新照片信息
 
		ProductPhotos productPhoto = new ProductPhotos();
		productPhoto.setPhotoName(fileName);
		productPhoto.setPhotoPath(filePath.toString());
		productPhoto.setProducts(product);
        productPhoto.setAdded(LocalDateTime.now());
        
         
		return productPhotosRepo.save(productPhoto);
	}
	
	@Transactional
    public void deleteProductPhoto(Integer photoId) throws IOException {
        ProductPhotos photo = productPhotosRepo.findById(photoId)
            .orElseThrow(() -> new RuntimeException("Product photo not found with id: " + photoId));

        Path filePath = Paths.get(uploadDir, photo.getPhotoPath().substring("/product_photos/".length()));
        Files.deleteIfExists(filePath);

        productPhotosRepo.delete(photo);
    }
	    
	    @Transactional
	    public ProductPhotos updateProductPhoto(Integer photoId, MultipartFile newFile) throws IOException {
	        if (newFile.isEmpty()) {
	            throw new IllegalArgumentException("New file is empty");
	        }

	        ProductPhotos photo = productPhotosRepo.findById(photoId)
	            .orElseThrow(() -> new RuntimeException("Product photo not found with id: " + photoId));

	        // Delete old file
	        Path oldFilePath = Paths.get(uploadDir, photo.getPhotoPath().substring("/product_photos/".length()));
	        Files.deleteIfExists(oldFilePath);

	        // Save new file
	        String fileName = StringUtils.cleanPath(newFile.getOriginalFilename());
	        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
	        Path uploadPath = Paths.get(uploadDir);
	        Path newFilePath = uploadPath.resolve(uniqueFileName);
	        newFile.transferTo(newFilePath.toFile());

	        // Update photo information
	       
	        photo.setPhotoName(fileName);
	        photo.setPhotoPath(newFilePath.toString());
	        photo.setAdded(LocalDateTime.now());

	        return productPhotosRepo.save(photo);
	    }
	    
	    public ProductPhotos getProductPhoto(Integer productId, Integer photoId) {
	        Product product = productRepo.findById(productId)
	            .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

	        return productPhotosRepo.findByProductsAndId(product, photoId)
	            .orElseThrow(() -> new RuntimeException("Product photo not found with productId: " + productId + " and photoId: " + photoId));
	    }
	    
	    public List<ProductPhotos> getAllProductPhotos(Integer productId) {
	        Product product = productRepo.findById(productId)
	            .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
	        return productPhotosRepo.findByProducts(product);
	    }
	    
	    public ProductPhotos getProductPhoto(Integer photoId) {
	        return productPhotosRepo.findById(photoId)
	            .orElseThrow(() -> new RuntimeException("Product photo not found with id: " + photoId));
	    }
	
	
}
