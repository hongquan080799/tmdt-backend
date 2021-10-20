package com.abc.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Sanpham {
	@Id
	@Column(name="masp")
	String masp;
	@Column(name="tensp")
	String tensp;
	
	Integer soluong;
	Float dongia;
	String mota_ngan;
	String mota_chitiet;
	Float khuyenmai;
	
	@OneToMany(mappedBy = "sanpham")
	List<Hinhanh> listHA;
	

	@ManyToOne
	@JoinColumn(name = "MADM")
	Danhmuc danhmuc;
	
	@JsonIgnore
	@OneToMany(mappedBy = "sanpham")
	List<CTDH> listCTDH;
	
	@JsonIgnore
	@OneToMany(mappedBy ="sanpham")
	List<Danhgia> listDanhGia;
	
	@JsonIgnore
	@OneToMany(mappedBy = "sanpham")
	List<Giohang> listGioHang;
	
	@JsonIgnore
	@OneToMany(mappedBy = "sanpham")
	List<CtPhatsinh> listCTPS;
	
	public void from(Sanpham sanpham) {
		this.tensp = sanpham.getTensp() != null ? sanpham.getTensp() : this.tensp;
		this.soluong = sanpham.getSoluong() != null ? sanpham.getSoluong() : this.soluong;
		this.dongia = sanpham.getDongia() != null ? sanpham.getDongia() : this.dongia;
		this.khuyenmai = sanpham.getKhuyenmai() != null ? sanpham.getKhuyenmai() : this.khuyenmai;
		this.mota_ngan = sanpham.getMota_ngan() != null ? sanpham.getMota_ngan() : this.mota_ngan;
		this.mota_chitiet = sanpham.getMota_chitiet() != null ? sanpham.getMota_chitiet() : this.mota_chitiet;
		this.danhmuc = sanpham.getDanhmuc() != null ? sanpham.getDanhmuc() : this.danhmuc;
//		List<Hinhanh> listHinhAnhUD = sanpham.getListHA();
//		if(!listHinhAnhUD.isEmpty()) {
//			for(Hinhanh ha : listHinhAnhUD) {
//				ha.setSanpham(this);
//			}
//			this.setListHA(listHinhAnhUD);
//		}
//		else 
//			this.setListHA(null);
	
	}
	


	public List<CtPhatsinh> getListCTPS() {
		return listCTPS;
	}



	public void setListCTPS(List<CtPhatsinh> listCTPS) {
		this.listCTPS = listCTPS;
	}



	public void setSoluong(Integer soluong) {
		this.soluong = soluong;
	}



	public void setDongia(Float dongia) {
		this.dongia = dongia;
	}



	public String getMasp() {
		return masp;
	}

	public void setMasp(String masp) {
		this.masp = masp;
	}

	public String getTensp() {
		return tensp;
	}

	public void setTensp(String tensp) {
		this.tensp = tensp;
	}

	public Integer getSoluong() {
		return soluong;
	}

	public void setSoluong(int soluong) {
		this.soluong = soluong;
	}

	public Float getDongia() {
		return dongia;
	}

	public void setDongia(float dongia) {
		this.dongia = dongia;
	}

	public String getMota_ngan() {
		return mota_ngan;
	}

	public void setMota_ngan(String mota_ngan) {
		this.mota_ngan = mota_ngan;
	}

	public String getMota_chitiet() {
		return mota_chitiet;
	}

	public void setMota_chitiet(String mota_chitiet) {
		this.mota_chitiet = mota_chitiet;
	}

	

	public List<Hinhanh> getListHA() {
		return listHA;
	}

	public void setListHA(List<Hinhanh> listHA) {
		this.listHA = listHA;
	}

	public Float getKhuyenmai() {
		return khuyenmai;
	}

	public void setKhuyenmai(Float khuyenmai) {
		this.khuyenmai = khuyenmai;
	}

	public Danhmuc getDanhmuc() {
		return danhmuc;
	}

	public void setDanhmuc(Danhmuc danhmuc) {
		this.danhmuc = danhmuc;
	}

	public List<CTDH> getListCTDH() {
		return listCTDH;
	}

	public void setListCTDH(List<CTDH> listCTDH) {
		this.listCTDH = listCTDH;
	}

	public List<Danhgia> getListDanhGia() {
		return listDanhGia;
	}

	public void setListDanhGia(List<Danhgia> listDanhGia) {
		this.listDanhGia = listDanhGia;
	}


	public List<Giohang> getListGioHang() {
		return listGioHang;
	}

	public void setListGioHang(List<Giohang> listGioHang) {
		this.listGioHang = listGioHang;
	}
	public Sanpham() {
		super();
	}
	
}
