package com.interverse.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.interverse.demo.model.ArticlePhoto;
import com.interverse.demo.model.ArticlePhotoRepository;
import com.interverse.demo.model.ClubArticle;
import com.interverse.demo.model.ClubArticlesRepository;

@Service
public class ArticlePhotoService {

	@Value("${upload.clubArticle.dir}")
	private String uploadDir;
	
	@Autowired
	private ArticlePhotoRepository articlePhotoRepo;
	
	@Autowired
	private ClubArticlesRepository articleRepo;
	
	public ArticlePhoto createArticlePhoto(MultipartFile file, Integer articleId) throws IOException {
		ClubArticle article = articleRepo.findById(articleId).orElseThrow(() -> new RuntimeException("ClubArticle not found with id:" + articleId));
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String uniqueFileName=UUID.randomUUID().toString() + "_" + fileName;
	
		Path uploadPath = Paths.get(uploadDir);
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		Path filePath = uploadPath.resolve(uniqueFileName);
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		
		ArticlePhoto articlePhoto = new ArticlePhoto();
		articlePhoto.setName(fileName);
		articlePhoto.setUrl(uploadDir + "/" + uniqueFileName);
		articlePhoto.setClubArticle(article);
	
		return articlePhotoRepo.save(articlePhoto);
	}
	
		public List<ArticlePhoto> findPhotoListByArticleId(Integer articleId){
			List<ArticlePhoto> list = articlePhotoRepo.findPhotoListByArticleId(articleId);
			return list;
		}
	

}
