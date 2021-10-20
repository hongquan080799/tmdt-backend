package com.abc.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Taikhoan {
	@Id
	String username;
	String password;
	
	@ManyToOne
	@JoinColumn(name = "maquyen")
	Quyen quyen;
	
	String verificationCode;
	
	int status;
	
	@JsonIgnore
	@OneToMany(mappedBy = "taikhoan",cascade = CascadeType.ALL)
	List<Khachhang> listKH;
	
	@JsonIgnore
	@OneToMany(mappedBy = "taikhoan",cascade = CascadeType.ALL)
	List<Nhanvien> listNV;
	


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Quyen getQuyen() {
		return quyen;
	}

	public void setQuyen(Quyen quyen) {
		this.quyen = quyen;
	}

	public List<Khachhang> getListKH() {
		return listKH;
	}

	public void setListKH(List<Khachhang> listKH) {
		this.listKH = listKH;
	}

	public List<Nhanvien> getListNV() {
		return listNV;
	}

	public void setListNV(List<Nhanvien> listNV) {
		this.listNV = listNV;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
}
