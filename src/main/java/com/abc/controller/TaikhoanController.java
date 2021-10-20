package com.abc.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abc.dto.UserDto;
import com.abc.entity.Diachi;
import com.abc.entity.Khachhang;
import com.abc.entity.Nhanvien;
import com.abc.entity.Quyen;
import com.abc.entity.Taikhoan;
import com.abc.repository.ChangePasswordRequest;
import com.abc.repository.DiachiRepository;
import com.abc.repository.TaikhoanRepository;
import com.abc.request.RegisterRequest;
import com.abc.response.CustomResponse;

import net.bytebuddy.utility.RandomString;

@RestController
@CrossOrigin
public class TaikhoanController {
	
	@Autowired 
	TaikhoanRepository taikhoanRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	DiachiRepository diachiRepository;
	
	@GetMapping("/user")
	public ResponseEntity<UserDto> getUser(Principal principal) {
		Taikhoan taikhoan = taikhoanRepo.findByUsername(principal.getName());
		UserDto user = new UserDto();
		if(taikhoan != null) {
			if(taikhoan.getQuyen().getMaquyen() == 1 ) {
				user.setUsername(taikhoan.getUsername());
				user.setDisplayname("ADMIN");
				user.setQuyen("ADMIN");
			}
			else if(taikhoan.getQuyen().getMaquyen() == 2) {
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
			}else if(taikhoan.getQuyen().getMaquyen() == 3) {
				Nhanvien nv = taikhoan.getListNV().get(0);
				user.setUsername(taikhoan.getUsername());
				user.setHo(nv.getHo());
				user.setTen(nv.getTen());
				user.setDiachi(nv.getDiachi());
				user.setDisplayname(nv.getHo() + " " + nv.getTen());
				user.setPhoto(nv.getPhoto());
				user.setSdt(nv.getSdt());
				user.setGioitinh(nv.getGioitinh());
				user.setQuyen("NHANVIEN");
				user.setId(nv.getId());
				user.setEmail(nv.getEmail());
			}
			return new ResponseEntity<UserDto>(user,HttpStatus.OK);
		}
		return new ResponseEntity<UserDto>(new UserDto(),HttpStatus.BAD_REQUEST);
		
	}
	@PutMapping("/taikhoan")
	public ResponseEntity<CustomResponse> getRegister(Principal principal ,HttpServletRequest httpServletRequest ,@Validated @RequestBody UserDto request){
		Taikhoan tk = taikhoanRepo.findByUsername(principal.getName());
		Khachhang kh = tk.getListKH().get(0);
		kh.setHo(request.getHo());
		kh.setTen(request.getTen());
		kh.setEmail(request.getEmail());
		kh.setGioitinh(request.getGioitinh());
		kh.setSdt(request.getSdt());
		kh.setPhoto(request.getPhoto());
		Diachi dc = null;
		if(!request.getListDC().isEmpty()) {
			dc = request.getListDC().get(0);
			dc.setKhachhang(kh);
			dc.setIsHomeAddress(true);
			if(dc.getId() != null) {
				Diachi dcDelete = new Diachi();
				for(Diachi d : kh.getListDC()) {
					if(d.getId().equals(dc.getId())) {
						dcDelete = d;
					}
				}
				kh.getListDC().remove(dcDelete);
			}
			if(dc != null)
				kh.getListDC().add(dc);
		}
//		request.getListDC().forEach(dc ->{
//		dc.setKhachhang(kh);
//		dc.setIsHomeAddress(true);
//	});
	
		try {
			taikhoanRepo.save(tk);
			return new ResponseEntity<CustomResponse>(new CustomResponse("Your account have been updated successfully !!! !!!"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<CustomResponse>(new CustomResponse("Failed to update an account !!!"),HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PutMapping("/nhanvien")
	public ResponseEntity<CustomResponse> updateNhanvien(Principal principal ,HttpServletRequest httpServletRequest ,@Validated @RequestBody UserDto request) throws Exception{
		Taikhoan tk = taikhoanRepo.findByUsername(principal.getName());
		if(tk.getListNV().isEmpty())
			throw new Exception("Permission deniel !");
		Nhanvien nv = tk.getListNV().get(0);
		nv.setHo(request.getHo());
		nv.setTen(request.getTen());
		nv.setEmail(request.getEmail());
		nv.setGioitinh(request.getGioitinh());
		nv.setSdt(request.getSdt());
		nv.setPhoto(request.getPhoto());
		nv.setDiachi(request.getDiachi());
		try {
			taikhoanRepo.save(tk);
			return new ResponseEntity<CustomResponse>(new CustomResponse("Your account have been updated successfully !!! !!!"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<CustomResponse>(new CustomResponse("Failed to update an account !!!"),HttpStatus.BAD_REQUEST);
		}
		
	}
	@PutMapping("/taikhoan/addShipAddress")
	public ResponseEntity<CustomResponse> getRegister(Principal principal, @Validated @RequestBody Diachi diachi){
		Taikhoan tk = taikhoanRepo.findByUsername(principal.getName());
		if(tk != null) {
			diachi.setIsShipAddress(false);
			diachi.setIsHomeAddress(false);
			diachi.setKhachhang(tk.getListKH().get(0));
			tk.getListKH().get(0).getListDC().add(diachi);
			try {
				taikhoanRepo.save(tk);
				return new ResponseEntity<CustomResponse>(new CustomResponse("Your account have been updated successfully !!! !!!"), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<CustomResponse>(new CustomResponse("Failed to update an account !!!"),HttpStatus.BAD_REQUEST);
	}
	@PutMapping("/taikhoan/setShipAddress")
	public ResponseEntity<CustomResponse> setShipAddress(Principal principal, @RequestParam("id") Integer id){
		Taikhoan tk = taikhoanRepo.findByUsername(principal.getName());
		if(tk != null) {
			tk.getListKH().get(0).getListDC().forEach(dc ->{
				if(dc.getId().equals(id))
					dc.setIsShipAddress(true);
				else
					dc.setIsShipAddress(false);
			});
			try {
				taikhoanRepo.save(tk);
				return new ResponseEntity<CustomResponse>(new CustomResponse("Your account have been updated successfully !!! !!!"), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<CustomResponse>(new CustomResponse("Failed to update an account !!!"),HttpStatus.BAD_REQUEST);
	}
	@DeleteMapping("/taikhoan/deleteShipAddress")
	public ResponseEntity<CustomResponse> deleteShipAddress(Principal principal, @RequestParam("id") Integer id){
		Taikhoan tk = taikhoanRepo.findByUsername(principal.getName());
		Diachi diachi = null;
		if(tk != null) {
			List<Diachi> listDC = tk.getListKH().get(0).getListDC();
			for(Diachi dc : listDC) {
				if(dc.getId().equals(id))
					diachi = dc;
			}
			listDC.remove(diachi);
			try {
				taikhoanRepo.save(tk);
				return new ResponseEntity<CustomResponse>(new CustomResponse("Your account have been updated successfully !!! !!!"), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<CustomResponse>(new CustomResponse("Failed to update an account !!!"),HttpStatus.BAD_REQUEST);
	}
	@PutMapping("/taikhoan/changePassword")
	public ResponseEntity<CustomResponse> changePassword(Principal principal, @Validated @RequestBody ChangePasswordRequest request) throws Exception{
		Taikhoan taikhoan = taikhoanRepo.findByUsername(principal.getName());
		if(taikhoan == null)
			throw new Exception("Account not exits !!!");
		taikhoan.setPassword(passwordEncoder.encode(request.getPassword()));
		
		try {
			taikhoanRepo.save(taikhoan);
			return new ResponseEntity<CustomResponse>(new CustomResponse("Your account have been updated successfully !!! !!!"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<CustomResponse>(new CustomResponse("Failed to update an account !!!"),HttpStatus.BAD_REQUEST);
		
	}
}
