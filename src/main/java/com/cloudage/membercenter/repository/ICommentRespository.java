package com.cloudage.membercenter.repository;

import java.util.List;

import org.omg.CORBA.OMGVMCID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cloudage.membercenter.entity.Admin;
import com.cloudage.membercenter.entity.Article;
import com.cloudage.membercenter.entity.Comment;

public interface ICommentRespository extends PagingAndSortingRepository<Comment, Integer>{

	@Query("from Comment comment where comment.article = ?1")
	List<Comment> findAllByArticle(Article article);
	
	@Query("from Comment comment where comment.article.id = ?1")
	List<Comment> findAllByArticleId(Integer id);
	
	@Query("from Comment comment where comment.article.id = ?1")
	Page<Comment> findAllOfArticleId(int article,Pageable page);
}
