package com.interverse.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interverse.demo.model.ClubArticle;
import com.interverse.demo.model.ClubArticleLike;
import com.interverse.demo.model.ClubArticleLikeRepository;
import com.interverse.demo.model.ClubArticlesRepository;
import com.interverse.demo.model.User;
import com.interverse.demo.model.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ClubArticleLikeService {

	@Autowired
	private ClubArticleLikeRepository articleLikeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ClubArticlesRepository articleRepository;

	@Transactional
	public void toggleLike(Integer userId, Integer articleId) {
		Optional<User> userOptional = userRepository.findById(userId);
		Optional<ClubArticle> articleOptional = articleRepository.findById(articleId);
		if (userOptional.isPresent() && articleOptional.isPresent()) {
			User user = userOptional.get();
			ClubArticle article = articleOptional.get();
			// 檢查是否已經存在喜好記錄
			boolean liked = articleLikeRepository.existsByUserIdAndArticleId(userId, articleId);
			ClubArticleLike articleLike = new ClubArticleLike();

			// 如果存在，則刪除喜好記錄
			if (liked) {
				articleLikeRepository.deleteByUserIdAndArticleId(userId, articleId);
				article.setLikeCount(article.getLikeCount() - 1);
			// 如果不存在，則創建新的喜好記錄
			} else {
				articleLike.setUser(user);
				articleLike.setArticle(article);
//            postLike.setType(type);
				article.setLikeCount(article.getLikeCount() + 1);
				articleLikeRepository.save(articleLike);
			}
			

			articleRepository.save(article);
		}
	}

	public boolean hasUserLikedArticle(Integer userId, Integer articleId) {
		return articleLikeRepository.existsByUserIdAndArticleId(userId, articleId);
	}

}
