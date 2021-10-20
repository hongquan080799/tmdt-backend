package com.abc.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abc.dto.UserDto;
import com.abc.entity.Diachi;
import com.abc.entity.Khachhang;
import com.abc.entity.Nhanvien;
import com.abc.entity.Quyen;
import com.abc.entity.Taikhoan;
import com.abc.repository.TaikhoanRepository;
import com.abc.request.RegisterNhanvienRequest;
import com.abc.request.RegisterRequest;
import com.abc.response.CustomResponse;
import com.abc.utils.SortUtils;

import net.bytebuddy.utility.RandomString;

@RestController
@CrossOrigin
public class NhanvienController {

	@Autowired
	TaikhoanRepository taikhoanRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@GetMapping("/nhanvien")
	public List<UserDto> getAllListNhanvien(
			@RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
			@RequestParam(name = "sortType", required = false, defaultValue = "asc") String sortType){
		List<Taikhoan> listTK = taikhoanRepository.findAllByStatus(1);
		List<UserDto> listNV = new ArrayList<>();
		for(Taikhoan taikhoan : listTK) {
			if(taikhoan.getListNV().size() >0) {
				UserDto user = new UserDto();
				Nhanvien nv = taikhoan.getListNV().get(0);
				user.setUsername(taikhoan.getUsername());
				user.setHo(nv.getHo());
				user.setTen(nv.getTen());
				user.setEmail(nv.getEmail());
				user.setDiachi(nv.getDiachi());
				user.setDisplayname(nv.getHo() + " " + nv.getTen());
				user.setPhoto(nv.getPhoto());
				user.setSdt(nv.getSdt());
				user.setGioitinh(nv.getGioitinh());
				user.setQuyen("KHACHHANG");
				user.setId(nv.getId());
				listNV.add(user);
			}
		}
		SortUtils.sort(listNV, sortType, sortField);
		return listNV;
	}
	@DeleteMapping("/nhanvien")
	public ResponseEntity<CustomResponse> deleteNhanvien(@RequestParam("username") String username){
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
	
	@PostMapping("/nhanvien")
	public ResponseEntity<CustomResponse> insertNhanvien(@RequestBody RegisterNhanvienRequest request){
		Taikhoan tk = new Taikhoan();
		Nhanvien nv = new Nhanvien();
		tk.setUsername(request.getUsername());
		tk.setPassword(passwordEncoder.encode(request.getPassword()));
		Quyen quyen = new Quyen();
		quyen.setMaquyen(3);
		tk.setQuyen(quyen);
		nv.setHo(request.getHo());
		nv.setTen(request.getTen());	
		nv.setEmail(request.getEmail());
		nv.setGioitinh(request.getGioitinh());
		nv.setSdt(request.getSdt());
		nv.setPhoto(request.getPhoto());
		nv.setTaikhoan(tk);
		nv.setDiachi(request.getDiachi());
		tk.setListNV(Arrays.asList(nv));
		tk.setStatus(1);
		
		try {
			taikhoanRepository.save(tk);
			return new ResponseEntity<CustomResponse>(new CustomResponse("Insert Employee successfully !!!"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<CustomResponse>(new CustomResponse("Insert Employee failed !!!"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
