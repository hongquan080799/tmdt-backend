package com.abc.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abc.dto.GiohangDto;
import com.abc.entity.Giohang;
import com.abc.entity.Giohang_ID;
import com.abc.entity.Khachhang;
import com.abc.entity.Sanpham;
import com.abc.repository.CartRepository;
import com.abc.repository.KhachhangRepository;
import com.abc.repository.SanphamRepository;
import com.abc.response.CustomResponse;

@CrossOrigin
@RestController
public class CartControler {
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	SanphamRepository sanphamRepository;
	
	@Autowired
	KhachhangRepository khachhangRepository;
	
	@GetMapping("/giohang")
	public List<Giohang> getListGHByMakh(Principal principal){
		String username = principal.getName();
		Integer makh = khachhangRepository.getMakhByusername(username);
		return cartRepository.findAllByMakh(makh);
	}
	
	
	@GetMapping("/numCart")
	public int getNumCart(Principal principal) {
		Integer num = 0;
		try {
			String username = principal.getName();
			Integer makh = khachhangRepository.getMakhByusername(username);
			num = cartRepository.getNumCart(makh);
			if(num.equals(null))
				return 0;
		} catch (Exception e) {
			return 0;
		}
		return num;
	}
	@PostMapping("/giohang")
	public ResponseEntity<CustomResponse> insertCart(Principal principal ,@RequestBody GiohangDto giohangDto) throws Exception{
		
		String username = principal.getName();
		Integer makh = khachhangRepository.getMakhByusername(username);
		giohangDto.setMakh(makh);
		
		Sanpham sp = sanphamRepository.findById(giohangDto.getMasp()).get();
		Khachhang kh = khachhangRepository.findById(giohangDto.getMakh()).get();
		
		Giohang_ID id = new Giohang_ID();
		id.setMakh(kh.getId());
		id.setMasp(sp.getMasp());
		
		Giohang gh = new Giohang();
		if(cartRepository.existsById(id)) {
			gh = cartRepository.findById(id).get();
			gh.setSoluong(gh.getSoluong() + giohangDto.getSoluong());
		}
		else {
			gh.setId(id);
			gh.setKhachhang(kh);
			gh.setSanpham(sp);
			gh.setSoluong(giohangDto.getSoluong());
		}
		try {
			cartRepository.save(gh);
			return new ResponseEntity<CustomResponse>(new CustomResponse("Add to cart successfully !"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<CustomResponse>(new CustomResponse("Add to cart failed !"), HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping("/giohang")
	public ResponseEntity<CustomResponse> updateGiohang(Principal principal,@Validated @RequestBody GiohangDto giohangDto){
		String username = principal.getName();
		Integer makh = khachhangRepository.getMakhByusername(username);
		giohangDto.setMakh(makh);
		
		Sanpham sp = sanphamRepository.findById(giohangDto.getMasp()).get();
		Khachhang kh = khachhangRepository.findById(giohangDto.getMakh()).get();
		
		Giohang_ID id = new Giohang_ID();
		id.setMakh(kh.getId());
		id.setMasp(sp.getMasp());
		
		Giohang gh = new Giohang();
		if(cartRepository.existsById(id)) {
			gh = cartRepository.findById(id).get();
			gh.setSoluong(giohangDto.getSoluong());
		}
		else {
			return new ResponseEntity<CustomResponse>(new CustomResponse("Product not exits !"), HttpStatus.BAD_REQUEST);
		}
		try {
			cartRepository.save(gh);
			return new ResponseEntity<CustomResponse>(new CustomResponse("update cart successfully !"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<CustomResponse>(new CustomResponse("update cart failed !"), HttpStatus.BAD_REQUEST);
	}
	
	@DeleteMapping("/giohang")
	public ResponseEntity<CustomResponse> deleteGiohang(Principal principal, @RequestParam("masp") String masp){
		String username = principal.getName();
		Integer makh = khachhangRepository.getMakhByusername(username);
		
		
		Giohang_ID id = new Giohang_ID();
		id.setMakh(makh);
		id.setMasp(masp);
		try {
			cartRepository.deleteById(id);
			return new ResponseEntity<CustomResponse>(new CustomResponse("delete cart successfully !"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<CustomResponse>(new CustomResponse("delete cart failed !"), HttpStatus.BAD_REQUEST);
	}
	
}
