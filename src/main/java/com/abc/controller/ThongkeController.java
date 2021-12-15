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
		String sql = "select s.MASP, s.TENSP , round(sum(c.SOLUONG * c.GIA), 2 )from " +
					"sanpham s join ctdh c on s.MASP = c.MASP " +
					"JOIN donhang d on d.MADH = c.MADH " +
					"where d.NGAYDAT >= ?1 and d.NGAYDAT <= ?2 " +
					"GROUP by s.MASP";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1, from);
		query.setParameter(2, to);
		
		List<Object[]> list = query.getResultList();
		List<ThongkeResponse> myList = new ArrayList<>();
		for(Object[] result : list) {
			ThongkeResponse tk = new ThongkeResponse(result);
			myList.add(tk);
		}
		
		return myList;
	}
	
	@GetMapping("/lailo")
	public List<LailoResponse> getListResponseLaiLo(@RequestParam("from") Date from, @RequestParam("to") Date to){
		String sql =
				"select s.MASP, s.TENSP , round(sum(c.SOLUONG * c.GIA - cp.SOLUONG * cp.DONGIA) , 2 ) as tonggia from " +
				"sanpham s " +
				"join ctdh c on s.MASP = c.MASP " +
				"JOIN donhang d on d.MADH = c.MADH " +
				"JOIN ct_phatsinh cp on cp.MASP = s.MASP " +
				"JOIN phatsinh p on p.ID = cp.MAPHIEU " +
				"where d.NGAYDAT >= ?1 " +
				"and d.NGAYDAT <= ?2 " +
				"and p.NGAY  >= ?1 " +
				"and p.NGAY <= ?2 " +
				"GROUP by s.MASP " +
				"ORDER by tonggia ASC ";
		Query query = entityManager.createNativeQuery(sql);
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
