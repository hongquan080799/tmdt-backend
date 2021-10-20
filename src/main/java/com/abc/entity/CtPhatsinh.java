package com.abc.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.abc.request.CtpsRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CtPhatsinh {
	@EmbeddedId
	CtpsID id;
	
	Integer soluong;
	Float dongia;
	
	@ManyToOne
	@MapsId("masp")
	@JoinColumn(name ="masp")
	Sanpham sanpham;
	
	
	@ManyToOne
	@MapsId("maphieu")
	@JoinColumn(name = "maphieu")
	Phatsinh phatsinh;

	
	public void update(CtpsRequest request) {
		this.id.setMasp(request.getMasp() != null ? request.getMasp() : this.getSanpham().getMasp());
		this.sanpham.setMasp(request.getMasp() != null ? request.getMasp() : this.getSanpham().getMasp());
		this.setSoluong(request.getSoluong() != null ? request.getSoluong() : this.getSoluong());
		this.setDongia(request.getDongia() != null ? request.getDongia() : this.getDongia());
	}
	
	public CtpsID getId() {
		return id;
	}

	public void setId(CtpsID id) {
		this.id = id;
	}

	public Integer getSoluong() {
		return soluong;
	}

	public void setSoluong(Integer soluong) {
		this.soluong = soluong;
	}

	public Float getDongia() {
		return dongia;
	}

	public void setDongia(Float dongia) {
		this.dongia = dongia;
	}

	public Sanpham getSanpham() {
		return sanpham;
	}

	public void setSanpham(Sanpham sanpham) {
		this.sanpham = sanpham;
	}

	public Phatsinh getPhatsinh() {
		return phatsinh;
	}

	public void setPhatsinh(Phatsinh phatsinh) {
		this.phatsinh = phatsinh;
	}
	
	
}
