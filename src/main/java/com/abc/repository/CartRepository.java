package com.abc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.abc.entity.Giohang;
import com.abc.entity.Giohang_ID;

public interface CartRepository extends JpaRepository<Giohang, Giohang_ID>{
	@Query("select gh from Giohang gh where gh.id.makh = ?1")
	List<Giohang> findAllByMakh(Integer makh);
	
	@Query("select sum(gh.soluong) from Giohang gh where gh.id.makh = ?1")
	Integer getNumCart(Integer makh);
}
