package com.abc.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abc.entity.CTDH_ID;
import com.abc.entity.DanhGia_ID;
import com.abc.entity.Danhgia;
import com.abc.entity.Donhang;
import com.abc.entity.Khachhang;
import com.abc.entity.Sanpham;
import com.abc.entity.Taikhoan;
import com.abc.repository.CtdhRepository;
import com.abc.repository.DanhgiaRepository;
import com.abc.repository.DonhangRepository;
import com.abc.repository.KhachhangRepository;
import com.abc.repository.TaikhoanRepository;

@RestController
@CrossOrigin
@RequestMapping("/rate")
public class RateController {
	
	@Autowired
	DanhgiaRepository danhgiaRepository;
	
	@Autowired
	DonhangRepository donhangRepository;
	
	@Autowired 
	TaikhoanRepository taikhoanRepository;
	
	@Autowired
	KhachhangRepository khachhangRepository;
	@GetMapping("/isEnableRate")
	public Boolean isEnableRate(@RequestParam("masp") String masp, Principal principal) {
		Taikhoan tk = taikhoanRepository.findByUsername(principal.getName());
		if(tk.getListKH() != null) {
			Khachhang kh = tk.getListKH().get(0);
			if(donhangRepository.isEnableRate(kh, masp) > 0)
				return true;
		}
		return false;
	}
	@GetMapping
	public int getDanhGiaByKH(@RequestParam("masp") String masp, Principal principal) {
		Integer makh = khachhangRepository.getMakhByusername(principal.getName());
		DanhGia_ID id = new DanhGia_ID();
		id.setMakh(makh);
		id.setMasp(masp);
		Optional<Danhgia> dg = danhgiaRepository.findById(id);
		if(dg.isEmpty()) {
			return 0;
		}
		else
			return dg.get().getDanhgia();
	}
	
	@GetMapping("/all")
	public com.abc.response.ResponseDanhGia getAllDanhGia(@RequestParam("masp") String masp){
		com.abc.response.ResponseDanhGia dg = new com.abc.response.ResponseDanhGia();
		Sanpham sp = new Sanpham();
		sp.setMasp(masp);
		
		List<Danhgia> listDG = danhgiaRepository.findAllBySanpham(sp);		
		float one=0,two=0,three=0,four=0,five=0;
		
		for(Danhgia d : listDG) {
			if(d.getDanhgia() == 1)
				one++;
			else if(d.getDanhgia() == 2)
				two++;
			else if(d.getDanhgia() == 3)
				three++;
			else if(d.getDanhgia() == 4)
				four++;
			else if(d.getDanhgia() ==5)
				five++;
		}
		int soluong = (int) (one + two + three + four +five);
		if(soluong != 0) {
			one = (one/soluong) *100;
			two = (two/soluong) *100;
			three = (three/soluong) *100;
			four = (four/soluong) *100;
			five = (five/soluong) *100;
		}
		dg.setOne(one);
		dg.setTwo(two);
		dg.setThree(three);
		dg.setFour(four);
		dg.setFive(five);
		dg.setSoluong(soluong);
		
		
		return dg;
	}
	
	@PostMapping
	public ResponseEntity<Boolean> insertDanhgia(@Validated @RequestBody Danhgia danhgia, Principal principal){
		Integer makh = khachhangRepository.getMakhByusername(principal.getName());
		danhgia.getId().setMakh(makh);
		danhgia.getKhachhang().setId(makh);
		List<Danhgia> listDG = danhgiaRepository.findAll();
		try {
			danhgiaRepository.save(danhgia);
			return new ResponseEntity<Boolean>(true,HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<Boolean>(false,HttpStatus.BAD_REQUEST);
		}
		
	}
}
