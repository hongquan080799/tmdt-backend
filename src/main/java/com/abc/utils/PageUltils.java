package com.abc.utils;




import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUltils {
	public static Pageable page(int pageNum, int pageSize, String sortBy, String sortType) {
		Pageable pageable = null;
		if(sortType.equalsIgnoreCase("asc"))
			return PageRequest.of(pageNum, pageSize, Sort.by(sortBy).ascending());
		else if(sortType.equalsIgnoreCase("desc"))
			return PageRequest.of(pageNum, pageSize, Sort.by(sortBy).descending());
		return pageable;
	}
}
