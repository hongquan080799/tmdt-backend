package com.abc.request;

import java.util.List;

public class DathangRequest {
	public static class Sanpham{
		String masp;
		int soluong;
		float dongia;
		public String getMasp() {
			return masp;
		}
		public void setMasp(String masp) {
			this.masp = masp;
		}
		public int getSoluong() {
			return soluong;
		}
		public void setSoluong(int soluong) {
			this.soluong = soluong;
		}
		public float getDongia() {
			return dongia;
		}
		public void setDongia(float dongia) {
			this.dongia = dongia;
		}
	}
	
	List<Sanpham> listSP;
	String madhGhn;
	String diachi;
	int httt; //hinh thuc thanh toan;
	public List<Sanpham> getListSP() {
		return listSP;
	}
	public void setListSP(List<Sanpham> listSP) {
		this.listSP = listSP;
	}
	public int getHttt() {
		return httt;
	}
	public void setHttt(int httt) {
		this.httt = httt;
	}
	public String getMadhGhn() {
		return madhGhn;
	}
	public void setMadhGhn(String madhGhn) {
		this.madhGhn = madhGhn;
	}
	public String getDiachi() {
		return diachi;
	}
	public void setDiachi(String diachi) {
		this.diachi = diachi;
	}
	
	
	
}
