package com.abc.response;

public class FacebookLoginResponse {
	String id;
	String first_name;
	String middle_name;
	String last_name;
	String email;
	FacebookPicture picture;
	
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getFirst_name() {
		return first_name;
	}


	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}


	public String getLast_name() {
		return last_name;
	}


	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public FacebookPicture getPicture() {
		return picture;
	}


	public void setPicture(FacebookPicture picture) {
		this.picture = picture;
	}


	public String getMiddle_name() {
		return middle_name;
	}


	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}
	
	

	

}
