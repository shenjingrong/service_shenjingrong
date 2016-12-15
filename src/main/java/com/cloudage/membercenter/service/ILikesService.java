package com.cloudage.membercenter.service;

import com.cloudage.membercenter.entity.Article;
import com.cloudage.membercenter.entity.Likes;
import com.cloudage.membercenter.entity.User;

public interface ILikesService {
	void addLike (Article article,User user);
	void removeLike (Article article,User user);
	Integer countLike(int articleId);
	Integer checkLike(int articleId,int userId);
}
