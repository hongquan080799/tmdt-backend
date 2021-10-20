package com.abc.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abc.entity.Diachi;
import com.abc.entity.Khachhang;
import com.abc.entity.Quyen;
import com.abc.entity.Taikhoan;
import com.abc.repository.TaikhoanRepository;
import com.abc.request.RegisterRequest;
import com.abc.response.CustomResponse;
import com.abc.service.MailService;

import net.bytebuddy.utility.RandomString;

@RestController
@CrossOrigin
public class RegisterController {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	TaikhoanRepository repo;
	
	@Autowired
	MailService mailService;
	
	
	@PostMapping("/register")
	public ResponseEntity<CustomResponse> getRegister(HttpServletRequest httpServletRequest ,@Validated @RequestBody RegisterRequest request){
		Taikhoan tk = new Taikhoan();
		Khachhang kh = new Khachhang();
		tk.setUsername(request.getUsername());
		tk.setPassword(passwordEncoder.encode(request.getPassword()));
		Quyen quyen = new Quyen();
		quyen.setMaquyen(2);
		tk.setQuyen(quyen);
		kh.setHo(request.getHo());
		kh.setTen(request.getTen());
		
		List<Diachi> listDC = new ArrayList<>();
		
		for(RegisterRequest.Diachi dc : request.getListDC()) {
			Diachi diachi = new Diachi();
			diachi.setProvinceId(dc.getProvinceId());
			diachi.setProvinceName(dc.getProvinceName());
			diachi.setDistrictId(dc.getDistrictId());
			diachi.setWardCode(dc.getWardCode());
			diachi.setWardName(dc.getWardName());
			diachi.setAddressDetail(dc.getAddressDetail());
			diachi.setIsShipAddress(true);
			diachi.setIsHomeAddress(true);
			diachi.setKhachhang(kh);
			listDC.add(diachi);
		}
		
		kh.setSdt(request.getSdt());
		kh.setEmail(request.getEmail());
		kh.setGioitinh(request.getGioitinh());
		kh.setPhoto(request.getPhoto());
		kh.setTaikhoan(tk);
		kh.setListDC(listDC);
		tk.setListKH(Arrays.asList(kh));
		
		String verificationCode = tk.getUsername() + RandomString.make(50);
		tk.setVerificationCode(verificationCode);
		String urlSite = httpServletRequest.getRequestURL().toString().replace(httpServletRequest.getServletPath(), "");
		try {
			repo.save(tk);
			mailService.sendVerificationMail(kh.getEmail(),kh.getHo() + " " + kh.getTen(), urlSite, tk.getUsername());
			return new ResponseEntity<CustomResponse>(new CustomResponse("Your account have been created successfully !!! !!!"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<CustomResponse>(new CustomResponse("Failed to create an account !!!"),HttpStatus.BAD_REQUEST);
		}
		
	}
	@GetMapping("/verify")
	public void verificationTaikhoan(@RequestParam("code") String verificationCode,HttpServletResponse response) {
		String redirectURL = "https://localhost:3000/login";
		Taikhoan tk = repo.findByVerificationCode(verificationCode);
		tk.setStatus(1);
		try {
			repo.save(tk);
			response.sendRedirect(redirectURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
