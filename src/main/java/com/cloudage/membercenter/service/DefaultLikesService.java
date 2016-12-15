package com.cloudage.membercenter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudage.membercenter.entity.Article;
import com.cloudage.membercenter.entity.Likes;
import com.cloudage.membercenter.entity.Likes.Key;
import com.cloudage.membercenter.entity.User;
import com.cloudage.membercenter.repository.ILikeRepository;

@Component
@Service
@Transactional
public class DefaultLikesService implements ILikesService{

	@Autowired
	ILikeRepository iLikeRepo;

	@Override
	public void addLike(Article article, User user) {
		Key key = new Key();
		key.setArticle(article);
		key.setUser(user);
		
		Likes lk = new Likes();
		lk.setId(key);
		
		iLikeRepo.save(lk);
		
	}

	@Override
	public void removeLike(Article article, User user) {
		Key key = new Key();
		key.setArticle(article);
		key.setUser(user);
		
		iLikeRepo.delete(key);
	}

	@Override
	public Integer countLike(int articleId) {
		return iLikeRepo.likeCountsOfArticle(articleId);
	}

	@Override
	public Integer checkLike(int articleId, int userId) {
		return iLikeRepo.checkLikesExsists(userId, articleId);
	}
	

}
