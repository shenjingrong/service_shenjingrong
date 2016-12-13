package com.cloudage.membercenter.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.cloudage.membercenter.entity.Article;
import com.cloudage.membercenter.entity.Comment;

public interface ICommentService {

	Comment save(Comment comment);
	List<Comment> findAllByArticle(Article article);
	List<Comment> findAllByArticleId(Integer articleId);
	Page<Comment> getComments(int article,Integer pageId);
}
