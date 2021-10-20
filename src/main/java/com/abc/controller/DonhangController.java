package com.abc.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abc.entity.CTDH;
import com.abc.entity.CTDH_ID;
import com.abc.entity.Donhang;
import com.abc.entity.Giohang;
import com.abc.entity.Giohang_ID;
import com.abc.entity.Khachhang;
import com.abc.entity.Taikhoan;
import com.abc.repository.CartRepository;
import com.abc.repository.DonhangRepository;
import com.abc.repository.KhachhangRepository;
import com.abc.repository.SanphamRepository;
import com.abc.repository.TaikhoanRepository;
import com.abc.request.DathangRequest;
import com.abc.request.DathangRequest.Sanpham;
import com.abc.response.CustomResponse;



@RestController
@CrossOrigin
public class DonhangController {
	
	@Autowired
	KhachhangRepository khachhangRepository;
	
	@Autowired
	DonhangRepository donhangRepository;
	
	@Autowired 
	CartRepository cartRepository;
	
	@Autowired
	TaikhoanRepository taikhoanRepository;
	
	@Autowired
	SanphamRepository sanphamRepository;
	
	@GetMapping("/donhang")
	public List<Donhang> getAllListDonhang(
			@RequestParam(name = "sortField", required = false, defaultValue = "ngaydat") String sortField,
			@RequestParam(name = "sortType", required = false, defaultValue = "desc") String sortType){
		Sort sort = Sort.by(sortType.equalsIgnoreCase("asc")?Direction.ASC:Direction.DESC, sortField);
		return donhangRepository.findAll(sort);
	}
	
	@GetMapping("/donhang/khachhang")
	public List<Donhang> getDonhangByKhachhang(Principal principal) {
		String username = principal.getName();
		Khachhang khachhang = khachhangRepository.getKhachhangByUsername(username);
		
		return donhangRepository.findAllByKhachhang(khachhang);	
	}
	
	@PostMapping("/donhang")
	ResponseEntity<CustomResponse> getOrder(Principal principal, @Validated @RequestBody DathangRequest request) throws Exception{
		String madh = "DH" +  System.currentTimeMillis() % 100000000;
		String username = principal.getName();
		Khachhang khachhang = khachhangRepository.getKhachhangByUsername(username);
		Donhang donhang = new Donhang();
		
		float tongtien = 0f;
		List<CTDH> listCTDH = new ArrayList<>();
		List<Giohang_ID> listGH = new ArrayList<>();
		for(DathangRequest.Sanpham sp : request.getListSP()) {
			
			com.abc.entity.Sanpham sanphamqua = sanphamRepository.findById(sp.getMasp()).get();
			if(sanphamqua.getSoluong() < sp.getSoluong() ) {
				return new ResponseEntity<CustomResponse>(new CustomResponse("Product number not enough !!!"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			CTDH_ID id = new CTDH_ID();
			CTDH ct = new CTDH();
			tongtien += sp.getSoluong() * sp.getDongia();
			id.setMadh(madh);
			id.setMasp(sp.getMasp());
			ct.setId(id);
			com.abc.entity.Sanpham sanpham = new com.abc.entity.Sanpham();
			sanpham.setMasp(sp.getMasp());
			ct.setSanpham(sanpham);
			ct.setSoluong(sp.getSoluong());
			ct.setDonhang(donhang);
			ct.setGia(sp.getDongia());
			listCTDH.add(ct);
			
			
			Giohang_ID ghID = new Giohang_ID();
			ghID.setMakh(khachhang.getId());
			ghID.setMasp(sp.getMasp());
			
			listGH.add(ghID);
		}
		
		donhang.setDiachi(request.getDiachi());
		donhang.setMadhGhn(request.getMadhGhn());
		donhang.setTongtien(tongtien);
		donhang.setKhachhang(khachhang);
		donhang.setMadh(madh);
		donhang.setHinhthucthanhtoan(request.getHttt());
		donhang.setTrangthai(0);
		donhang.setListCTDH(listCTDH);
		//
		
		
		try {
			donhangRepository.save(donhang);
			cartRepository.deleteAllById(listGH);
			return new ResponseEntity<CustomResponse>(new CustomResponse("Add order successfully !!!"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<CustomResponse>(new CustomResponse("Add order successfully !!!"), HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@PutMapping("/donhang")
	public ResponseEntity<CustomResponse> updateDonhang(Principal principal ,@RequestParam("madh") String madh, @RequestParam("trangthai") Integer trangthai){
		Taikhoan tk = taikhoanRepository.findByUsername(principal.getName());
		
		
		Optional<Donhang> donhangOptional = donhangRepository.findById(madh);
		if(donhangOptional.isPresent()) {
			
			Donhang dh = donhangOptional.get();
			if(tk.getQuyen().getMaquyen() == 3) {
				dh.setNhanvien(tk.getListNV().get(0));
			}
			dh.setTrangthai(trangthai);
			try {
				donhangRepository.save(dh);
				return new ResponseEntity<CustomResponse>(new CustomResponse("Update order successfully !"), HttpStatus.OK);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return new ResponseEntity<CustomResponse>(new CustomResponse("Update order failed !"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
