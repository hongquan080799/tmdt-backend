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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Nhanvien {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	String ho;
	String ten;
	String sdt;
	String email;
	String photo;
	int gioitinh;
	float luong;
	
	@JsonIgnore
	@OneToMany(mappedBy = "nhanvien")
	List<Donhang> listDH;
	
	@ManyToOne
	@JoinColumn(name = "username")
	Taikhoan taikhoan;
	
	@JsonIgnore
	@OneToMany(mappedBy = "nhanvien")
	List<Phatsinh> listPS;

	String diachi;
	 
	
	
	

	
	public List<Phatsinh> getListPS() {
		return listPS;
	}

	public void setListPS(List<Phatsinh> listPS) {
		this.listPS = listPS;
	}

	public String getDiachi() {
		return diachi;
	}

	public void setDiachi(String diachi) {
		this.diachi = diachi;
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

	public float getLuong() {
		return luong;
	}

	public void setLuong(float luong) {
		this.luong = luong;
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
