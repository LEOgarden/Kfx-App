package com.kfx.android.bean;

import java.util.Date;

public class Mail {
	private String title;
	private String text;
	private String photo;
	private String top;
	private Date create;
	private Date update;
	private Date autoDelete;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getTop() {
		return top;
	}
	public void setTop(String top) {
		this.top = top;
	}
	public Date getCreate() {
		return create;
	}
	public void setCreate(Date create) {
		this.create = create;
	}
	public Date getUpdate() {
		return update;
	}
	public void setUpdate(Date update) {
		this.update = update;
	}
	public Date getAutoDelete() {
		return autoDelete;
	}
	public void setAutoDelete(Date autoDelete) {
		this.autoDelete = autoDelete;
	}
	
}
