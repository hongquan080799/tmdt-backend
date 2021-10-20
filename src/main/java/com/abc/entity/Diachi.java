package com.abc.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Diachi {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer id;
	Integer provinceId;
	String provinceName;
	Integer districtId;
	String districtName;
	String wardCode;
	String wardName;
	String addressDetail;
	Boolean isHomeAddress;
	Boolean isShipAddress;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "makh")
	Khachhang khachhang;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}



	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public Khachhang getKhachhang() {
		return khachhang;
	}

	public void setKhachhang(Khachhang khachhang) {
		this.khachhang = khachhang;
	}


	public String getWardCode() {
		return wardCode;
	}

	public void setWardCode(String wardCode) {
		this.wardCode = wardCode;
	}

	public Boolean getIsHomeAddress() {
		return isHomeAddress;
	}

	public void setIsHomeAddress(Boolean isHomeAddress) {
		this.isHomeAddress = isHomeAddress;
	}

	public Boolean getIsShipAddress() {
		return isShipAddress;
	}

	public void setIsShipAddress(Boolean isShipAddress) {
		this.isShipAddress = isShipAddress;
	}
	
	
	
	
}
