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

import com.abc.entity.CtPhatsinh;
import com.abc.entity.CtpsID;
import com.abc.entity.Nhanvien;
import com.abc.entity.Phatsinh;
import com.abc.entity.Sanpham;
import com.abc.entity.Taikhoan;
import com.abc.repository.CtPhatsinhRepository;
import com.abc.repository.PhatsinhRepository;
import com.abc.repository.TaikhoanRepository;
import com.abc.request.CtpsRequest;
import com.abc.response.CustomResponse;

@RestController
@CrossOrigin
public class CtpsController {

	@Autowired
	CtPhatsinhRepository ctPhatsinhRepository;
	
	@Autowired
	PhatsinhRepository phatsinhRepository;
	
	@Autowired
	TaikhoanRepository taikhoanRepository;
	
	@GetMapping("/ctps")
	public List<CtPhatsinh> getListCtPhatsinh(@RequestParam("maphieu") Integer maphieu){
		return ctPhatsinhRepository.getListCtpsByMaPhieu(maphieu);
	}
	
	@PostMapping("/ctps")
	public ResponseEntity<CustomResponse> insertCTPS(@Validated @RequestBody CtpsRequest request, Principal principal) throws Exception{
		Taikhoan tk = taikhoanRepository.findByUsername(principal.getName());
		CtPhatsinh ctPhatsinh = new CtPhatsinh();
		CtpsID id = new CtpsID(request.getMaphieu(), request.getMasp());
		ctPhatsinh.setId(id);
		ctPhatsinh.setSoluong(request.getSoluong());
		ctPhatsinh.setDongia(request.getDongia());
		Sanpham sp = new Sanpham();
		sp.setMasp(request.getMasp());
		Phatsinh ps = phatsinhRepository.findById(request.getMaphieu()).get();
		ctPhatsinh.setSanpham(sp);
		ctPhatsinh.setPhatsinh(ps);
		if(tk.getQuyen().getMaquyen() == 1) {
			try {
				ctPhatsinhRepository.save(ctPhatsinh);
				return new ResponseEntity<CustomResponse>(new CustomResponse("Create ctps successfully !"), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(tk.getQuyen().getMaquyen() == 3){
			Nhanvien nhanvien = tk.getListNV().get(0);
			if(nhanvien.getId() != ctPhatsinh.getPhatsinh().getNhanvien().getId())
				throw new Exception("Can not create because it not belong to this employee ");
			try {
				ctPhatsinhRepository.save(ctPhatsinh);
				return new ResponseEntity<CustomResponse>(new CustomResponse("Insert ctps successfully !"), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return new ResponseEntity<CustomResponse>(new CustomResponse("Insert ctps failed !"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@PutMapping("/ctps")
	public ResponseEntity<CustomResponse> updateCTPS(@Validated @RequestBody CtpsRequest request, Principal principal) throws Exception{
		Taikhoan tk = taikhoanRepository.findByUsername(principal.getName());
		CtPhatsinh ctPhatsinh = new CtPhatsinh();
		CtpsID id = new CtpsID(request.getMaphieu(), request.getMasp());
		Optional<CtPhatsinh> optional = ctPhatsinhRepository.findById(id);
		if(optional.isEmpty())
			throw new Exception("Can't find ctps");
		
		ctPhatsinh = optional.get();
		ctPhatsinh.update(request);
		if(tk.getQuyen().getMaquyen() == 1) {
			try {
				ctPhatsinhRepository.save(ctPhatsinh);
				return new ResponseEntity<CustomResponse>(new CustomResponse("Update ctps successfully !"), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(tk.getQuyen().getMaquyen() == 3){
			Nhanvien nhanvien = tk.getListNV().get(0);
			if(nhanvien.getId() != ctPhatsinh.getPhatsinh().getNhanvien().getId())
				throw new Exception("Can not update because it not belong to this employee ");
			try {
				ctPhatsinhRepository.save(ctPhatsinh);
				return new ResponseEntity<CustomResponse>(new CustomResponse("Update ctps successfully !"), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return new ResponseEntity<CustomResponse>(new CustomResponse("Update ctps failed !"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@DeleteMapping("/ctps")
	public ResponseEntity<CustomResponse> deleteCTPS(Principal principal, @RequestParam("maphieu") Integer maphieu, @RequestParam("masp") String masp) throws Exception{
		CtpsID id = new CtpsID(maphieu, masp);
		Taikhoan tk = taikhoanRepository.findByUsername(principal.getName());
		CtPhatsinh ctPhatsinh = new CtPhatsinh();
		Optional<CtPhatsinh> optional = ctPhatsinhRepository.findById(id);
		if(optional.isEmpty())
			throw new Exception("Can't find ctps");
		
		ctPhatsinh = optional.get();
		if(tk.getQuyen().getMaquyen() == 1) {
			try {
				ctPhatsinhRepository.deleteById(id);
				return new ResponseEntity<CustomResponse>(new CustomResponse("Delete ctps successfully !"), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(tk.getQuyen().getMaquyen() == 3){
			Nhanvien nhanvien = tk.getListNV().get(0);
			if(nhanvien.getId() != ctPhatsinh.getPhatsinh().getNhanvien().getId())
				throw new Exception("Can not delete because it not belong to this employee ");
			try {
				ctPhatsinhRepository.deleteById(id);
				return new ResponseEntity<CustomResponse>(new CustomResponse("Delete ctps successfully !"), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return new ResponseEntity<CustomResponse>(new CustomResponse("Update ctps failed !"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
