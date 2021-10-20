package com.abc.response;

import com.abc.entity.Taikhoan;

public class LoginResponse {
	String jwt;

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public LoginResponse(String jwt) {
		this.jwt = jwt;
	}

	
	
}
