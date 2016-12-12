package com.cloudage.membercenter.service;

import java.util.List;

import com.cloudage.membercenter.entity.Article;
import com.cloudage.membercenter.entity.User;

public interface IArticleService {
	Article save(Article article);
	List<Article> findAllByAuthor(User user);
	List<Article> findAllByAuthorId(Integer userId);
}
