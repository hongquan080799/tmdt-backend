package com.abc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.abc.entity.CtPhatsinh;
import com.abc.entity.CtpsID;

public interface CtPhatsinhRepository extends JpaRepository<CtPhatsinh, CtpsID>{
	@Query("select ct from CtPhatsinh ct where ct.id.maphieu = ?1")
	List<CtPhatsinh> getListCtpsByMaPhieu(Integer maphieu);
}
