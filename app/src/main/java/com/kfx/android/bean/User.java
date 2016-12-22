package com.kfx.android.bean;

public class User {
	private long id;
	private String name;
	private int role;
	private String certificateid;
	private String telp;
	private String password;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public String getCertificateid() {
		return certificateid;
	}
	public void setCertificateid(String certificateid) {
		this.certificateid = certificateid;
	}
	public String getTelp() {
		return telp;
	}
	public void setTelp(String telp) {
		this.telp = telp;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
