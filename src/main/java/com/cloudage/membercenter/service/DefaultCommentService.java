package com.cloudage.membercenter.service;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudage.membercenter.entity.Article;
import com.cloudage.membercenter.entity.Comment;
import com.cloudage.membercenter.repository.ICommentRespository;

@Component
@Service
@Transactional
public class DefaultCommentService implements ICommentService{
	@Autowired
	ICommentRespository commentRepo;
	
	public Comment save(Comment comment) {
		return commentRepo.save(comment);
	}

	@Override
	public List<Comment> findAllByArticle(Article article) {
		return commentRepo.findAllByArticle(article);
	}

	@Override
	public List<Comment> findAllByArticleId(Integer articleId) {
		return commentRepo.findAllByArticleId(articleId);
	}
	
	@Override
	public Page<Comment> getComments(int articleid,Integer pageId) {
		Sort sort = new Sort(Direction.DESC,"createDate");
		PageRequest pageRequest = new PageRequest(pageId, 10,sort);
		return commentRepo.findAllOfArticleId(articleid,pageRequest);
	}

}
