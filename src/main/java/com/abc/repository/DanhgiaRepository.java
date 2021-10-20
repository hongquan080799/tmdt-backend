package com.abc.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.abc.entity.DanhGia_ID;
import com.abc.entity.Danhgia;
import com.abc.entity.Sanpham;

public interface DanhgiaRepository extends JpaRepository<Danhgia, DanhGia_ID>{
	List<Danhgia> findAllBySanpham(Sanpham sanpham);
	
}
