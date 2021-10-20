package com.abc.controller;

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abc.repository.DonhangRepository;
import com.abc.response.LailoResponse;
import com.abc.response.ThongkeResponse;

@RestController
@CrossOrigin
public class ThongkeController {
	@Autowired
	EntityManager entityManager;
	
	@Autowired
	DonhangRepository donhangRepository;
	
	@GetMapping("/thongke")
	public List<ThongkeResponse> getListResponseDanhGia(@RequestParam("from") Date from, @RequestParam("to") Date to){
		Query query = entityManager.createNativeQuery("call SP_THONGKE (?1,?2)");
		query.setParameter(1, from);
		query.setParameter(2, to);
		
		List<Object[]> list = query.getResultList();
		List<ThongkeResponse> myList = new ArrayList<>();
		for(Object[] result : list) {
			ThongkeResponse tk = new ThongkeResponse();
			tk.setMasp((String)result[0]);
			tk.setTensp((String) result[1]);
			tk.setDoanhthu(((Double) result[2]).doubleValue());
			myList.add(tk);
		}
		
		return myList;
	}
	
	@GetMapping("/lailo")
	public List<LailoResponse> getListResponseLaiLo(@RequestParam("from") Date from, @RequestParam("to") Date to){
		Query query = entityManager.createNativeQuery("call SP_LAILO (?1,?2)");
		query.setParameter(1, from);
		query.setParameter(2, to);
		
		List<Object[]> list = query.getResultList();
		List<LailoResponse> myList = new ArrayList<>();
		for(Object[] result : list) {
			LailoResponse tk = new LailoResponse();
			tk.setMasp((String)result[0]);
			tk.setTensp((String) result[1]);
			tk.setSotien(((Double) result[2]).doubleValue());
			myList.add(tk);
		}
		
		return myList;
	}
	
	
}
