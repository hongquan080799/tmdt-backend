package com.abc.controller;

import java.security.Principal;
import java.util.*;

import com.abc.client.MomoClient;
import com.abc.client.PaypalClient;
import com.abc.entity.*;
import com.abc.repository.*;
import com.google.gson.Gson;

import org.apache.tomcat.util.buf.Utf8Decoder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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

	@Autowired
	PaypalClient paypalClient;

	@Autowired
	VoucherRepository voucherRepository;
	
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

	@PostMapping("/donhang/getPayUrl")
	public ResponseEntity<?> getPayUrl(Principal principal, @RequestBody Object object, @RequestParam("httt") Integer httt) {
		if(httt.equals(2))
			return new ResponseEntity<>(MomoClient.getPayUrl(object), HttpStatus.OK);
		else if(httt.equals(1))
			return new ResponseEntity<>(paypalClient.getPayLink(new JSONObject(new Gson().toJson(object)).getString("link"), new JSONObject(new Gson().toJson(object)).getFloat("price"),principal.getName()), HttpStatus.OK);
		else
			return new ResponseEntity<>(new CustomResponse("Type of payment not valid !"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@GetMapping("/forwardMobile")
	public String forwardMobile(@RequestParam("extraData") String extraData, @RequestParam("PayerID") String payerId, @RequestParam("username") String username){
		// ModelAndView modelAndView = new ModelAndView();
		DathangRequest dathangRequest = new Gson().fromJson(Utf8.decode(Base64.getDecoder().decode(extraData)), DathangRequest.class);
		String refundLink = paypalClient.submitOrder(username, payerId);
		String madh = "DH" +  System.currentTimeMillis() % 100000000;
		Khachhang khachhang = khachhangRepository.getKhachhangByUsername(username);
		Donhang donhang = new Donhang();
		Voucher voucher = null;
		if(dathangRequest.getVoucherId() != null){
			 voucher = voucherRepository.findById(dathangRequest.getVoucherId()).get();
			donhang.setVoucher(voucher);
		}
		float tongtien = 0f;
		List<CTDH> listCTDH = new ArrayList<>();
		List<Giohang_ID> listGH = new ArrayList<>();
		for(DathangRequest.Sanpham sp : dathangRequest.getListSP()) {
			
			com.abc.entity.Sanpham sanphamqua = sanphamRepository.findById(sp.getMasp()).get();
//			if(sanphamqua.getSoluong() < sp.getSoluong() ) {
//				return new ResponseEntity<CustomResponse>(new CustomResponse("Product number not enough !!!"), HttpStatus.INTERNAL_SERVER_ERROR);
//			}
			
			CTDH_ID id = new CTDH_ID();
			CTDH ct = new CTDH();
			tongtien += sp.getSoluong() * sp.getDongia() -  sp.getSoluong() * sp.getDongia() * sanphamqua.getKhuyenmai();
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
		
		donhang.setDiachi(dathangRequest.getDiachi());
		donhang.setMadhGhn(dathangRequest.getMadhGhn());
		donhang.setTongtien(voucher != null ? tongtien * (1 - voucher.getDiscount()) : tongtien);
		donhang.setKhachhang(khachhang);
		donhang.setMadh(madh);
		donhang.setHinhthucthanhtoan(dathangRequest.getHttt());
		donhang.setTrangthai(0);
		donhang.setListCTDH(listCTDH);
		donhang.setRefundLinkPaypal(refundLink);
		//
		
		
		try {
			donhangRepository.save(donhang);
			for(Giohang_ID id : listGH){
				if(cartRepository.existsById(id))
					cartRepository.deleteById(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "<h1>Your order has been confirm successfully !!!</h1>";
	}
	@PostMapping("/donhang")
	ResponseEntity<CustomResponse> getOrder(Principal principal, @Validated @RequestBody DathangRequest request,@RequestParam(required = false, name = "payerId") String payerId) throws Exception{
		String refundLink = "";
		if(request.getHttt() == 1){
			//submit order paypal
			refundLink = paypalClient.submitOrder(principal.getName(), payerId);
			if(refundLink.isEmpty())
				return new ResponseEntity<CustomResponse>(new CustomResponse("Add order successfully !!!"), HttpStatus.INTERNAL_SERVER_ERROR);


		}
		String madh = "DH" +  System.currentTimeMillis() % 100000000;
		String username = principal.getName();
		Khachhang khachhang = khachhangRepository.getKhachhangByUsername(username);
		Donhang donhang = new Donhang();
		Voucher voucher = null;
		if(request.getVoucherId() != null){
			 voucher = voucherRepository.findById(request.getVoucherId()).get();
			donhang.setVoucher(voucher);
		}
		float tongtien = 0f;
		List<CTDH> listCTDH = new ArrayList<>();
		List<Giohang_ID> listGH = new ArrayList<>();
		for(DathangRequest.Sanpham sp : request.getListSP()) {
			
			com.abc.entity.Sanpham sanphamqua = sanphamRepository.findById(sp.getMasp()).get();
//			if(sanphamqua.getSoluong() < sp.getSoluong() ) {
//				return new ResponseEntity<CustomResponse>(new CustomResponse("Product number not enough !!!"), HttpStatus.INTERNAL_SERVER_ERROR);
//			}
			
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
		donhang.setTongtien(voucher != null ? tongtien * (1 - voucher.getDiscount()) : tongtien);
		donhang.setKhachhang(khachhang);
		donhang.setMadh(madh);
		donhang.setHinhthucthanhtoan(request.getHttt());
		donhang.setTrangthai(0);
		donhang.setListCTDH(listCTDH);
		donhang.setRefundLinkPaypal(refundLink);

		//
		
		
		try {
			donhangRepository.save(donhang);
			for(Giohang_ID id : listGH){
				if(cartRepository.existsById(id))
					cartRepository.deleteById(id);
			}
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
			if(trangthai == 4 && dh.getHinhthucthanhtoan() == 1 && !dh.getRefundLinkPaypal().isEmpty()){
				paypalClient.cashBack(dh.getRefundLinkPaypal());
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
