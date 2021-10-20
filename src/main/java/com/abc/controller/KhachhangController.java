package com.abc.controller;

import java.awt.print.Pageable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abc.dto.UserDto;
import com.abc.entity.Khachhang;
import com.abc.entity.Taikhoan;
import com.abc.repository.TaikhoanRepository;
import com.abc.response.CustomResponse;
import com.abc.utils.SortUtils;

@RestController
@CrossOrigin
public class KhachhangController {
	
	@Autowired
	TaikhoanRepository taikhoanRepository;
	
	@GetMapping("/khachhang")
	public List<UserDto> getAllListKhachhang(
			@RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
			@RequestParam(name = "sortType", required = false, defaultValue = "asc") String sortType) {
		List<Taikhoan> listTK = taikhoanRepository.findAllByStatus(1);
		List<UserDto> listKH = new ArrayList<>();
		for(Taikhoan taikhoan : listTK) {
			if(taikhoan.getListKH().size() >0) {
				UserDto user = new UserDto();
				Khachhang kh = taikhoan.getListKH().get(0);
				user.setUsername(taikhoan.getUsername());
				user.setHo(kh.getHo());
				user.setTen(kh.getTen());
				user.setEmail(kh.getEmail());
				user.setListDC(kh.getListDC());
				user.setDisplayname(kh.getHo() + " " + kh.getTen());
				user.setPhoto(kh.getPhoto());
				user.setSdt(kh.getSdt());
				user.setGioitinh(kh.getGioitinh());
				user.setQuyen("KHACHHANG");
				user.setId(kh.getId());
				listKH.add(user);
			}
		}
		SortUtils.sort(listKH, sortType, sortField);
		return listKH;
	
	}
	@DeleteMapping("/khachhang")
	public ResponseEntity<CustomResponse> deleteKhachhang(@RequestParam("username") String username){
		try {
			Taikhoan taikhoan = taikhoanRepository.findByUsername(username);
			taikhoan.setStatus(0);
			taikhoanRepository.save(taikhoan);
			return new ResponseEntity<CustomResponse>(new CustomResponse("Delete customer successfully !!!"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<CustomResponse>(new CustomResponse("Delete customer failed !!!"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
