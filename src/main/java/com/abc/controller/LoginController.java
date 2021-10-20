package com.abc.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.abc.client.SocialClient;
import com.abc.entity.Khachhang;
import com.abc.entity.Quyen;
import com.abc.entity.Taikhoan;
import com.abc.jwt.configs.CustomUserDetail;
import com.abc.jwt.configs.JwtTokenProvider;
import com.abc.jwt.configs.MyUserDetailsService;
import com.abc.repository.QuyenRepository;
import com.abc.repository.TaikhoanRepository;
import com.abc.request.LoginRequest;
import com.abc.request.SocialLoginRequest;
import com.abc.response.FacebookLoginResponse;
import com.abc.response.GoogleLoginResponse;
import com.abc.response.LoginResponse;

@RestController
@CrossOrigin
public class LoginController {
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	MyUserDetailsService userDetailsService;
	
	@Autowired
	TaikhoanRepository taikhoanRepo;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	QuyenRepository quyenRepo;
	
	@PostMapping("/login")
	public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest) throws Exception {
		System.out.println(passwordEncoder.encode("123"));;
		System.out.println("ok");
		try {
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		} catch (Exception e) {
			throw new Exception("Incorrect Username , Password",e);
		}
		CustomUserDetail userDetails = (CustomUserDetail) userDetailsService.loadUserByUsername(loginRequest.getUsername());
		String jwt = jwtTokenProvider.generateToken(userDetails);
		return new LoginResponse(jwt);
		
	}
	@GetMapping("/hello")
	public String hello() {
		return "hello world";
	}
	@PostMapping("/socialLogin")
	public LoginResponse socialLogin(@RequestBody SocialLoginRequest loginRequest) throws Exception{
		SocialClient client = new SocialClient();
		//validate token
		if(loginRequest.getType().equalsIgnoreCase("google")) {
			GoogleLoginResponse response = client.getGoogleinfo(loginRequest.getToken());
			if(response != null) {
				String googleUser = "GO"+response.getId();
				Taikhoan taikhoan = taikhoanRepo.findByUsername(googleUser);
				
				// check if it has the user or not
				if(taikhoan == null) {
					taikhoan = new Taikhoan();
					taikhoan.setUsername(googleUser);
					Quyen quyen = quyenRepo.findById(2).get();
					taikhoan.setQuyen(quyen);
					taikhoan.setPassword(passwordEncoder.encode("123"));
					taikhoan.setStatus(1);
					Khachhang kh = new Khachhang();
					kh.setPhoto(response.getPicture());
					kh.setTaikhoan(taikhoan);
					kh.setEmail(response.getEmail());
					kh.setHo(response.getFamily_name());
					kh.setTen(response.getGiven_name());
					taikhoan.setListKH(Arrays.asList(kh));
					taikhoanRepo.save(taikhoan);
				}
				
				CustomUserDetail userDetails = (CustomUserDetail) userDetailsService.loadUserByUsername(taikhoan.getUsername());
				String jwt = jwtTokenProvider.generateToken(userDetails);
				return new LoginResponse(jwt);
			}
			
		}
		else if(loginRequest.getType().equalsIgnoreCase("facebook")){
			FacebookLoginResponse response = client.getFacebookInfo(loginRequest.getToken());
			
			if(response != null) {
				String facebookUser =  "FB" + response.getId();
				Taikhoan taikhoan = taikhoanRepo.findByUsername(facebookUser);
				if(taikhoan == null) {
					taikhoan = new Taikhoan();
					taikhoan = new Taikhoan();
					taikhoan.setUsername(facebookUser);
					taikhoan.setStatus(1);
					Quyen quyen = quyenRepo.findById(2).get();
					taikhoan.setQuyen(quyen);
					taikhoan.setPassword(passwordEncoder.encode("123"));
					
					Khachhang kh = new Khachhang();
					kh.setTaikhoan(taikhoan);
					kh.setPhoto(response.getPicture().getData().getUrl());
					kh.setEmail(response.getEmail());
					kh.setHo(response.getLast_name() + " " + response.getMiddle_name());
					kh.setTen(response.getFirst_name());
					taikhoan.setListKH(Arrays.asList(kh));
					taikhoanRepo.save(taikhoan);
				}
				
				CustomUserDetail userDetails = (CustomUserDetail) userDetailsService.loadUserByUsername(taikhoan.getUsername());
				String jwt = jwtTokenProvider.generateToken(userDetails);
				return new LoginResponse(jwt);
			}
		}
		return null;
	}
}
