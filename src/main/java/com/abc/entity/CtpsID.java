package com.abc.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class CtpsID implements Serializable  {
	Integer maphieu;
	String masp;
	
	
	public CtpsID() {
		super();
	}
	
	public CtpsID(Integer maphieu, String masp) {
		super();
		this.maphieu = maphieu;
		this.masp = masp;
	}

	public Integer getMaphieu() {
		return maphieu;
	}
	public void setMaphieu(Integer maphieu) {
		this.maphieu = maphieu;
	}
	public String getMasp() {
		return masp;
	}
	public void setMasp(String masp) {
		this.masp = masp;
	}
	
	
}
