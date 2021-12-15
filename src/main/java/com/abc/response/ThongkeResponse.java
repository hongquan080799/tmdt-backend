package com.abc.response;

public class ThongkeResponse {
	String masp;
	String tensp;
	Double doanhthu;
	public ThongkeResponse(Object[] object){
		this.masp = (String) object[0];
		this.tensp = (String) object[1];
		this.doanhthu = (Double) object[2];
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
	public Double getDoanhthu() {
		return doanhthu;
	}
	public void setDoanhthu(Double doanhthu) {
		this.doanhthu = doanhthu;
	}
	
	
	
}
