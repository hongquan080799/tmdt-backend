package com.abc.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abc.entity.Taikhoan;
import com.abc.repository.DonhangRepository;
import com.abc.repository.TaikhoanRepository;
import com.abc.response.ThongkeResponse;

@RestController
public class MyController {

//	@Autowired
//	DonhangRepository donhangRepository;
//	@Autowired
//	EntityManager entityManager;
//	@GetMapping("/rate/quan")
//	public void home() throws ParseException {
//		Date from_date = new SimpleDateFormat("yyyy-MM-dd").parse("1999-01-01");
//		Date end_date = new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01");
//		System.out.println(from_date.toString());
//		List<ThongkeResponse> tk =  donhangRepository.getThongke(new Date(1999, 01, 01), new Date(2022, 01, 01));
//		
//		Query query = entityManager.createNativeQuery("call SP_THONGKE (?1,?2)");
//		query.setParameter(1, from_date);
//		query.setParameter(2, end_date);
//		
//		List<ThongkeResponse> tk1 =query.getResultList();
	//}
}
