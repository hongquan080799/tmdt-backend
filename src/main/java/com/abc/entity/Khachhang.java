package com.abc.entity;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Khachhang {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	String ho;
	String ten;
	String sdt;
	String email;
	String photo;
	int gioitinh;
	
	@OneToMany(mappedBy = "khachhang", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Diachi> listDC;
	
	@JsonIgnore
	@OneToMany(mappedBy = "khachhang")
	List<Danhgia> listDG;
	
	
	@JsonIgnore
	@OneToMany(mappedBy = "khachhang")
	List<Giohang> listGH;
	
	@JsonIgnore
	@OneToMany(mappedBy = "khachhang")
	List<Donhang> listDH;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "username")
	Taikhoan taikhoan;

	

	
	
	public List<Diachi> getListDC() {
		return listDC;
	}

	public void setListDC(List<Diachi> listDC) {
		this.listDC = listDC;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHo() {
		return ho;
	}

	public void setHo(String ho) {
		this.ho = ho;
	}

	public String getTen() {
		return ten;
	}

	public void setTen(String ten) {
		this.ten = ten;
	}


	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getGioitinh() {
		return gioitinh;
	}

	public void setGioitinh(int gioitinh) {
		this.gioitinh = gioitinh;
	}

	public List<Danhgia> getListDG() {
		return listDG;
	}

	public void setListDG(List<Danhgia> listDG) {
		this.listDG = listDG;
	}


	public List<Giohang> getListGH() {
		return listGH;
	}

	public void setListGH(List<Giohang> listGH) {
		this.listGH = listGH;
	}

	public List<Donhang> getListDH() {
		return listDH;
	}

	public void setListDH(List<Donhang> listDH) {
		this.listDH = listDH;
	}

	public Taikhoan getTaikhoan() {
		return taikhoan;
	}

	public void setTaikhoan(Taikhoan taikhoan) {
		this.taikhoan = taikhoan;
	}
	
	
	
}
