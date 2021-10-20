package com.abc.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Phatsinh {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer Id;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	Date ngay;
	String loai;
	
	@ManyToOne
	@JoinColumn(name = "MANV")
	Nhanvien nhanvien;
	
	@JsonIgnore
	@OneToMany(mappedBy = "phatsinh")
	List<CtPhatsinh> listCTPS;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	
	public Date getNgay() {
		return ngay;
	}

	public void setNgay(Date ngay) {
		this.ngay = ngay;
	}

	public String getLoai() {
		return loai;
	}

	public void setLoai(String loai) {
		this.loai = loai;
	}

	public Nhanvien getNhanvien() {
		return nhanvien;
	}

	public void setNhanvien(Nhanvien nhanvien) {
		this.nhanvien = nhanvien;
	}

	public List<CtPhatsinh> getListCTPS() {
		return listCTPS;
	}

	public void setListCTPS(List<CtPhatsinh> listCTPS) {
		this.listCTPS = listCTPS;
	}
	
	

}
