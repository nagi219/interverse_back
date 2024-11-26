package com.interverse.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.interverse.demo.dto.ArticlePhotoDTO;
import com.interverse.demo.dto.ClubArticleDTO;
import com.interverse.demo.model.ArticlePhoto;
import com.interverse.demo.model.ClubArticle;
import com.interverse.demo.service.ClubArticleService;




@RestController
@RequestMapping("/club/article")
public class ClubArticleController {

	@Autowired
	private ClubArticleService articleService;
	
    @PostMapping
    public ResponseEntity<?> addArticle(@RequestBody ClubArticleDTO articleDTO) {
        try {
        	String saveContent = articleDTO.getContent().replaceAll("\\r\\n|\\r|\\n", "\n");
        	articleDTO.setContent(saveContent);
        	ClubArticleDTO savedArticle = articleService.createArticle(articleDTO);
            return new ResponseEntity<>(savedArticle, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("創建文章時發生錯誤");
        }
    }
	
	@GetMapping("/all/{clubId}")
	public ResponseEntity<List<ClubArticleDTO>> showClubAllArticle(@PathVariable Integer clubId) {
	    try {
	        List<ClubArticleDTO> articleDTOs = articleService.findAllArticleByClubId(clubId);
	        articleService.loadBase64Photos(articleDTOs);
	        
	        return ResponseEntity.ok(articleDTOs);
	    } catch (IOException e) {
//	        System.err.println("Controller: Error loading photos: " + e.getMessage());
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    } catch (Exception e) {
//	        System.err.println("Controller: Unexpected error: " + e.getMessage());
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
	
	@GetMapping("/oneArticle/{articleId}")
	public ResponseEntity<ClubArticleDTO> findArticleById(@PathVariable Integer articleId) throws IOException{
		ClubArticleDTO articleDTO = articleService.findArticleById(articleId);
		if(articleDTO != null) {
			List<ArticlePhotoDTO> photos = articleDTO.getPhotos();
			for(ArticlePhotoDTO photo : photos) {
				File file = new File(photo.getUrl());
				byte[] photoFile = Files.readAllBytes(file.toPath());
				String base64Photo = "data:image/png;base64," + Base64.getEncoder().encodeToString(photoFile);
				photo.setBase64Photo(base64Photo);
			}
			
			return ResponseEntity.ok(articleDTO);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
    @PutMapping("/{articleId}")
    public ResponseEntity<ClubArticleDTO> updateArticle(@PathVariable Integer articleId,
                                    @RequestBody ClubArticleDTO clubArticleDTO) {
        ClubArticleDTO updatedArticle = articleService.updateArticle(articleId, clubArticleDTO);
        return ResponseEntity.ok(updatedArticle);
    }
	
	@DeleteMapping("/{articleId}")
	public void deleteArticle(@PathVariable Integer articleId) {
		articleService.deleteArticleById(articleId);
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<ClubArticleDTO>> searchArticles(@RequestParam String title, @RequestParam Integer clubId) throws IOException {
	     List<ClubArticleDTO> articleDTOs = articleService.searchArticlesByTitleAndClubId(title, clubId);
	        articleService.loadBase64Photos(articleDTOs);

	     
	     
//	     for(ClubArticleDTO article: articleDTOs) {
//	    	 List<ArticlePhotoDTO> photos = article.getPhotos();
//	    	 for(ArticlePhotoDTO photo:photos) {
//	    		 File file = new File(photo.getUrl());
//	    		 byte[] photoFile = Files.readAllBytes(file.toPath());
//	    		 String base64Photo = "data:image/png;base64," + Base64.getEncoder().encodeToString(photoFile);
//	    		 photo.setBase64Photo(base64Photo);
//	    	 }
//	     }
	    return ResponseEntity.ok(articleDTOs);
	}
    
    @PostMapping("/{articleId}/hashtags")
    public ResponseEntity<?> addHashtagsToArticle(@PathVariable Integer articleId, @RequestBody Set<String> hashtags) {
        try {
            ClubArticleDTO updatedArticle = articleService.addHashtagsToArticle(articleId, hashtags);
            return ResponseEntity.ok(updatedArticle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("添加hashtags時發生錯誤");
        }
    }

    @DeleteMapping("/{articleId}/hashtags")
    public ResponseEntity<?> removeHashtagsFromArticle(@PathVariable Integer articleId, @RequestBody Set<String> hashtags) {
        try {
            ClubArticleDTO updatedArticle = articleService.removeHashtagsFromArticle(articleId, hashtags);
            return ResponseEntity.ok(updatedArticle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("移除hashtags時發生錯誤");
        }
    }

    @GetMapping("/hashtag/{tag}")
    public ResponseEntity<List<ClubArticleDTO>> findArticlesByHashtag(@PathVariable String tag, @RequestParam Integer clubId) throws IOException {
        List<ClubArticleDTO> articles = articleService.findArticlesByHashtagAndClubId(tag, clubId);
        articleService.loadBase64Photos(articles);

        return ResponseEntity.ok(articles);
    }
	
}
