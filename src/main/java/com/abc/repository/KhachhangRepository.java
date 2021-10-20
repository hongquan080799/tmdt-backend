package com.abc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.abc.entity.Khachhang;

public interface KhachhangRepository extends JpaRepository<Khachhang, Integer>{
	
	@Query("select kh.id from Khachhang kh where kh.taikhoan.username = ?1")
	Integer getMakhByusername(String username);
	
	@Query("select kh from Khachhang kh where kh.taikhoan.username = ?1 ")
	Khachhang getKhachhangByUsername(String username);
}
