package com.abc.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import com.abc.entity.Donhang;
import com.abc.entity.Khachhang;
import com.abc.response.ThongkeResponse;
public interface DonhangRepository extends JpaRepository<Donhang, String>{
	List<Donhang> findAllByKhachhang(Khachhang khachhang);
	
	@Query("select count(ct) from CTDH ct where ct.donhang.khachhang = ?1 and ct.sanpham.masp = ?2")
	Integer isEnableRate(Khachhang khachhang, String masp);
	
//	@Query(value = "call SP_THONGKE (:tu_ngay, :den_ngay);",nativeQuery = true)
//	Object[] getThongke(Date tu_ngay, Date den_ngay, Sort sort);
}
