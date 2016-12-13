package com.cloudage.membercenter.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import com.cloudage.membercenter.util.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Comment extends BaseEntity{
	String content;
	
	Article article;
	User author;
	
	Date createDate;
	Date editDate;
	
	@ManyToOne(optional=false)
	public Article getArticle() {
		return article;
	}
	
	@ManyToOne(optional=false)
	public User getAuthor() {
		return author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(updatable=false)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getEditDate() {
		return editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public void setAuthor(User author) {
		this.author = author;
	}
	
	@PreUpdate
	void onPreUpdate(){
		editDate = new Date();
	}
	
	@PrePersist
	void onPrePersist(){
		createDate = new Date();
	}
	
}
