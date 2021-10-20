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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abc.entity.Nhanvien;
import com.abc.entity.Phatsinh;
import com.abc.entity.Taikhoan;
import com.abc.repository.PhatsinhRepository;
import com.abc.repository.TaikhoanRepository;
import com.abc.response.CustomResponse;

@RestController
@CrossOrigin
public class PhatsinhController {
	@Autowired
	PhatsinhRepository phatsinhRepository;
	
	@Autowired 
	TaikhoanRepository taikhoanRepository;
	@GetMapping("/phatsinh")
	public List<Phatsinh> getListPhatsinh(){
		return phatsinhRepository.findAll();
	}
	
	@PostMapping("/phatsinh")
	public ResponseEntity<CustomResponse> insertPhatsinh(@RequestParam("loai") String loai, Principal principal){
		Phatsinh phatsinh = new Phatsinh();
		phatsinh.setLoai(loai);
		Nhanvien nhanvien = null;
		Taikhoan tk = taikhoanRepository.findByUsername(principal.getName());
		if(!tk.getListNV().isEmpty())
			nhanvien = tk.getListNV().get(0);
		phatsinh.setNhanvien(nhanvien);
		
		try {
			phatsinhRepository.save(phatsinh);
			return new ResponseEntity<CustomResponse>(new CustomResponse("Create phatsinh successfully !"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<CustomResponse>(new CustomResponse("Create phatsinh failed !"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/phatsinh")
	public ResponseEntity<CustomResponse> deletePhatsinh(@RequestParam("id") Integer id, Principal principal) throws Exception{
		
		Optional<Phatsinh> optional = phatsinhRepository.findById(id);
		if(optional.isEmpty())
			throw new Exception("Can't find by id");
		
		Taikhoan tk = taikhoanRepository.findByUsername(principal.getName());
		if(tk.getQuyen().getMaquyen() == 2)
			throw new Exception("Permission deniel !");
		if(tk.getQuyen().getMaquyen() == 3) {
			Nhanvien nhanvien = tk.getListNV().get(0);
			if(nhanvien.getId() != optional.get().getNhanvien().getId())
				throw new Exception("Can not delete because it not belong to this employee");
		}
		try {
			phatsinhRepository.deleteById(id);
			return new ResponseEntity<CustomResponse>(new CustomResponse("Delete phatsinh successfully !"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<CustomResponse>(new CustomResponse("Delete phatsinh failed !"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
