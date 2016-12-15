package com.cloudage.membercenter.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.cloudage.membercenter.entity.Article;
import com.cloudage.membercenter.entity.User;

public interface IArticleService {
	Article save(Article article);
	List<Article> findAllByAuthor(User user);
	List<Article> findAllByAuthorId(Integer userId);
	Article findOne(int articleId);
	Page<Article>getFeeds(int page);
	Page<Article> searchArticlesByKeyword(String keyword,int page);
}
