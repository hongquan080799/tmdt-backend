package com.abc.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abc.entity.Danhmuc;
import com.abc.entity.Hinhanh;
import com.abc.entity.Sanpham;
import com.abc.repository.HinhanhRepository;
import com.abc.repository.SanphamRepository;
import com.abc.response.CustomResponse;
import com.abc.utils.PageUltils;

@RestController
@CrossOrigin
public class SanphamController {

	@Autowired
	RedisTemplate redisTemplate;

	@Autowired
	SanphamRepository sanphamRepository;
	
	@Autowired
	HinhanhRepository hinhanhRepository;
	
	@GetMapping("/sanpham")
	public List<Sanpham> getListSanPham(
			@RequestParam(value = "sortType", defaultValue = "asc", required = false) String sortType,
			@RequestParam(value = "sortBy", defaultValue = "masp", required = false) String sortBy){
		Sort sort = Sort.by(sortType.equalsIgnoreCase("asc")?Direction.ASC:Direction.DESC, sortBy);
		return sanphamRepository.findAll(sort);
	}
	
	@DeleteMapping("/sanpham")
	public ResponseEntity<CustomResponse> deleteSanpham(@RequestParam("masp") String masp){
		Sanpham sp = sanphamRepository.findById(masp).get();
		try {
			sanphamRepository.delete(sp);
			return new ResponseEntity<CustomResponse>(new CustomResponse("Delete Sanpham successfully !!!"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<CustomResponse>(new CustomResponse("Delete Sanpham failed !!!"), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping("/sanpham")
	public ResponseEntity<CustomResponse> insertSanpham(@Validated @RequestBody Sanpham sanpham){
		String masp = "SP" +  new Date().getTime() % 100000000;
		sanpham.setMasp(masp);
		try {
			sanphamRepository.save(sanpham);
			sanpham.getListHA().forEach(hinhanh ->{
				hinhanh.setSanpham(sanpham);
				hinhanhRepository.save(hinhanh);
			});
			return new ResponseEntity<CustomResponse>(new CustomResponse("Insert sanpham successfully !!!"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<CustomResponse>(new CustomResponse("Insert failed !!!"), HttpStatus.BAD_REQUEST);		
		}
	}
	
	@PutMapping("/sanpham")
	public ResponseEntity<CustomResponse> updateSanpham(@Validated @RequestBody Sanpham request){
		Sanpham sanpham = sanphamRepository.findById(request.getMasp()).get();
		sanpham.getListHA().forEach(hinhanh ->{
			hinhanhRepository.delete(hinhanh);
		});
		
		sanpham.setListHA(null);
		request.getListHA().forEach(hinhanh ->{
			hinhanh.setSanpham(sanpham);
			hinhanhRepository.save(hinhanh);
		});
		sanpham.from(request);
		try {
			sanphamRepository.save(sanpham);
			return new ResponseEntity<CustomResponse>(new CustomResponse("Update sanpham successfully !!!"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<CustomResponse>(new CustomResponse("Update failed !!!"), HttpStatus.BAD_REQUEST);	
			
		}
	}
	
	@GetMapping("/sanpham/{masp}")
	public Sanpham getSanphamByMasp(@PathVariable("masp") String masp) {
		//redisTemplate.opsForValue().set("name", "quan");

		return sanphamRepository.findById(masp).get();
	}
	
	@GetMapping("/danhmuc/{madm}")
	public List<Sanpham> getSanphamByMadm(@PathVariable("madm") String madm){
		Danhmuc dm = new Danhmuc();
		dm.setMadm(madm);
		return sanphamRepository.findAllByDanhmuc(dm);
	}
}
