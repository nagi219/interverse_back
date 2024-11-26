package com.interverse.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.interverse.demo.dto.ArticleCommentDTO;
import com.interverse.demo.model.ClubArticleComment;
import com.interverse.demo.model.ClubArticleCommentRepository;
import com.interverse.demo.model.ClubArticlesRepository;
import com.interverse.demo.model.UserRepository;

@Service
public class ClubArticlesCommentsService {

	@Autowired
	private ClubArticleCommentRepository commentRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ClubArticlesRepository articleRepo;
	
	public ArticleCommentDTO createComment(ArticleCommentDTO commentDTO) {
		ClubArticleComment comment = new ClubArticleComment();
		comment.setContent(commentDTO.getContent());
		comment.setUser(userRepo.findById(commentDTO.getUserId()).orElseThrow());
		comment.setClubArticle(articleRepo.findById(commentDTO.getArticleId()).orElseThrow());
	
		ClubArticleComment saveComment = commentRepo.save(comment);
		return ArticleCommentDTO.fromEntity(saveComment);
	}
	
	public ArticleCommentDTO findCommentById(Integer commentId) {
		return commentRepo.findById(commentId)
				.map(ArticleCommentDTO::fromEntity)
				.orElse(null);
	}
	
	public List<ArticleCommentDTO> findAllCommentByArticleIdd(Integer articleId){
		List<ClubArticleComment> comments = commentRepo.findAllCommentByArticleId(articleId);
		
		List<ArticleCommentDTO> collect = comments.stream()
											.map(ArticleCommentDTO::fromEntity)
											.collect(Collectors.toList());
		return collect;
	}
	
	@Transactional
	public ArticleCommentDTO updateCommentById(Integer commentId, ArticleCommentDTO commentDTO) {
		return commentRepo.findById(commentId)
				.map(comment ->{
					comment.setContent(commentDTO.getContent());
					return ArticleCommentDTO.fromEntity(comment);
				})
				.orElse(null);
	
	
	}
	
	public void deleteCommentById(Integer id) {
		commentRepo.deleteById(id);
	}
	

	
	
}
