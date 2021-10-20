package com.abc.controller;

import java.util.Date;
import java.util.List;

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

import com.abc.entity.Danhmuc;
import com.abc.repository.DanhmucRepository;
import com.abc.response.CustomResponse;

@RestController
@CrossOrigin
public class DanhmucController {
	
	@Autowired
	DanhmucRepository danhmucRepository;
	
	@GetMapping("/danhmuc")
	public List<Danhmuc> getListDanhmuc(){
		return danhmucRepository.findAll();
	}
	
	@PostMapping("/danhmuc")
	public ResponseEntity<CustomResponse> insertDanhmuc(@Validated @RequestBody Danhmuc danhmuc){
		String madm = "SP" +  new Date().getTime() % 100000000;
		danhmuc.setMadm(madm);
		try {
			danhmucRepository.save(danhmuc);
			return new ResponseEntity<CustomResponse>(new CustomResponse("Insert danhmuc successfully !!!"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<CustomResponse>(new CustomResponse("Insert danhmuc failed !!!"), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/danhmuc")
	public ResponseEntity<CustomResponse> updateDanhmuc(@Validated @RequestBody Danhmuc danhmuc){
		try {
			danhmucRepository.save(danhmuc);
			return new ResponseEntity<CustomResponse>(new CustomResponse("Update danhmuc successfully !!!"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<CustomResponse>(new CustomResponse("Update danhmuc failed !!!"), HttpStatus.BAD_REQUEST);
		}
	}
	@DeleteMapping("/danhmuc")
	public ResponseEntity<CustomResponse> deleteDanhmuc(@Validated @RequestParam("madm") String madm){
		try {
			danhmucRepository.deleteById(madm);
			return new ResponseEntity<CustomResponse>(new CustomResponse("Delete danhmuc successfully !!!"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<CustomResponse>(new CustomResponse("Delete danhmuc failed !!!"), HttpStatus.BAD_REQUEST);
		}
	}
}
