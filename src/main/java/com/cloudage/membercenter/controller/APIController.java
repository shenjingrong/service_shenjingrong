package com.cloudage.membercenter.controller;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import com.cloudage.membercenter.entity.Article;
import com.cloudage.membercenter.entity.Comment;
import com.cloudage.membercenter.entity.Likes;
import com.cloudage.membercenter.entity.Likes.Key;
import com.cloudage.membercenter.entity.User;
import com.cloudage.membercenter.service.IArticleService;
import com.cloudage.membercenter.service.ICommentService;
import com.cloudage.membercenter.service.ILikesService;
import com.cloudage.membercenter.service.IUserService;

@RestController
@RequestMapping("/api")
public class APIController {

	@Autowired
	IUserService userService;
	
	@Autowired
	IArticleService articleService;
	
	@Autowired
	ICommentService commentService;
	
	@Autowired
	ILikesService likesService;
	
	@RequestMapping(value = "/hello", method=RequestMethod.GET)
	public @ResponseBody String hello(){
		return "HELLO WORLD";
	}

	@RequestMapping(value="/register", method=RequestMethod.POST)
	public User register(
			@RequestParam String account,
			@RequestParam String passwordHash,
			@RequestParam String email,
			@RequestParam String name,
			MultipartFile avatar,
			HttpServletRequest request){
		
		User user = new User();
		user.setAccount(account);
		user.setPasswordHash(passwordHash);
		user.setEmail(email);
		user.setName(name);
		
		if(avatar!=null){
			try{
				String realPath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");
				FileUtils.copyInputStreamToFile(avatar.getInputStream(), new File(realPath,account+".png"));
				user.setAvatar("upload/"+account+".png");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return userService.save(user);
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public User login(
			@RequestParam String account,
			@RequestParam String passwordHash,
			HttpServletRequest request){
		
		User user = userService.findByAccount(account);
		if(user!=null && user.getPasswordHash().equals(passwordHash)){
			HttpSession session = request.getSession(true);
			session.setAttribute("uid", user.getId());
			return user;
		}else{
			return null;
		}
	}
	
	@RequestMapping(value="/me", method=RequestMethod.GET)
	public User getCurrentUser(HttpServletRequest request){
		HttpSession session = request.getSession(true);
		Integer uid = (Integer) session.getAttribute("uid");
		return userService.findById(uid);
	}
	
	@RequestMapping(value="/passwordrecover", method=RequestMethod.POST)
	public boolean resetPassword(
			@RequestParam String email,
			@RequestParam String passwordHash
			){
		User user = userService.findByEmail(email);
		if(user==null){
			return false;
		}else{
			user.setPasswordHash(passwordHash);
			userService.save(user);
			return true;
		}
	}
	
	@RequestMapping(value="/articles/{userId}")
	public List<Article> getArticlesByUserID(@PathVariable Integer userId){
		return articleService.findAllByAuthorId(userId);
	}
	
	@RequestMapping(value="/publisharticle",method=RequestMethod.POST)
	public Article publishArticle(
			@RequestParam String title,
			@RequestParam String text,
			HttpServletRequest request){
		Article article = new Article();
		article.setAuthor(getCurrentUser(request));
		article.setText(text);
		article.setTitle(title);
		articleService.save(article);
		
		return article;
	}
	
	@RequestMapping(value="/feeds/{page}")
	public Page<Article> getFeeds(@PathVariable int page){
		return articleService.getFeeds(page);
	}
	
	@RequestMapping(value="/feeds")
	public Page<Article> getFeeds(){
		return getFeeds(0);
	}
	
	@RequestMapping(value="/article/{article_id}/comments/{page}")
	public Page<Comment> getCommentOfArticle(
			@PathVariable int page,
			@PathVariable int article_id){
		return commentService.getComments(article_id,page);
	}
	
	@RequestMapping(value="/article/{article_id}/comments")
	public Page<Comment> getCommentOfArticle(
			@PathVariable int article_id){
		return commentService.getComments(article_id,0);
	}
	
	@RequestMapping(value="/article/{article_id}/comments",method = RequestMethod.POST)
	public Comment publishComment(
			@RequestParam String content,
			@PathVariable int article_id,
			HttpServletRequest request){
		Comment comment = new Comment();
		Article article = articleService.findOne(article_id);
		comment.setContent(content);
		comment.setAuthor(getCurrentUser(request));
		comment.setArticle(article);
		
		return commentService.save(comment);
	}
	
	@RequestMapping(value="/article/like/count/{article_id}")
	public Integer countLikes(
			@PathVariable int article_id){
		return likesService.countLike(article_id);
	}
	
	@RequestMapping(value="/article/{article_id}/like/checklike")
	public boolean checkLike(
			@PathVariable int article_id,
			HttpServletRequest request){
		boolean rs = false;
		int count = likesService.checkLike(article_id, getCurrentUser(request).getId());
		if(count!=0){
			rs = true;
		}
		return rs;
	}
	
	@RequestMapping(value="/article/likes/{article_id}",method = RequestMethod.POST)
	public Integer changeLikes(
			@PathVariable int article_id,
			@RequestParam boolean isLike,
			HttpServletRequest request){
		Article article = articleService.findOne(article_id);
		User user = getCurrentUser(request);
		
		if(isLike){
			likesService.addLike(article, user);
		}else {
			likesService.removeLike(article, user);
		}
		return likesService.countLike(article_id);
	}
	
	@RequestMapping("/article/s/{keyword}")
	public Page<Article> searchArticleByKeyword(
			@PathVariable String keyword,
			@RequestParam(defaultValue="0") int page){
		return articleService.searchArticlesByKeyword(keyword, page);
	}
}
