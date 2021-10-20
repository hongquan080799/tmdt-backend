package com.abc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abc.entity.Danhmuc;
import com.abc.entity.Sanpham;

public interface SanphamRepository extends JpaRepository<Sanpham, String>{

	List<Sanpham> findAllByDanhmuc(Danhmuc danhmuc);
}
