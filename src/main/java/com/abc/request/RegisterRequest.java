package com.abc.request;

import java.util.List;

public class RegisterRequest {
	
	public static class Diachi{
		Integer provinceId;
		String provinceName;
		Integer districtId;
		String districtName;
		String wardCode;
		String wardName;
		String addressDetail;
		Boolean isHomeAddress;
		Boolean isShipAddress;
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
		
		
		public String getWardCode() {
			return wardCode;
		}
		public void setWardCode(String wardCode) {
			this.wardCode = wardCode;
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

	String username;
	String password;
	String ho;
	String ten;
	int gioitinh;
	String sdt;
	String email;
	String photo;
	List<Diachi> listDC;
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
	public int getGioitinh() {
		return gioitinh;
	}
	public void setGioitinh(int gioitinh) {
		this.gioitinh = gioitinh;
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
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public List<Diachi> getListDC() {
		return listDC;
	}
	public void setListDC(List<Diachi> listDC) {
		this.listDC = listDC;
	}
	
	
	
	
	
}
