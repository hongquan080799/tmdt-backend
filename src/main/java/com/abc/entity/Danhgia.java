package com.abc.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
public class Danhgia {
	@EmbeddedId
	DanhGia_ID id;
	int danhgia;
	
	@ManyToOne
	@MapsId("masp")
	@JoinColumn(name = "masp")
	Sanpham sanpham;
	
	@ManyToOne
	@MapsId("makh")
	@JoinColumn(name = "makh")
	Khachhang khachhang;

	public DanhGia_ID getId() {
		return id;
	}

	public void setId(DanhGia_ID id) {
		this.id = id;
	}

	public int getDanhgia() {
		return danhgia;
	}

	public void setDanhgia(int danhgia) {
		this.danhgia = danhgia;
	}

	public Sanpham getSanpham() {
		return sanpham;
	}	

	public void setSanpham(Sanpham sanpham) {
		this.sanpham = sanpham;
	}

	public Khachhang getKhachhang() {
		return khachhang;
	}

	public void setKhachhang(Khachhang khachhang) {
		this.khachhang = khachhang;
	}

	
	
	
}
