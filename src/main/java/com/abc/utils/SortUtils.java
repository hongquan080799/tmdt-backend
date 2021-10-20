package com.abc.utils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class SortUtils {
	public static void sort(List<?> list, String sortType, String sortField) {
		Collections.sort(list, (o1, o2)->{
			try {
				Field f = o1.getClass().getDeclaredField(sortField);
				f.setAccessible(true);
				String oo1 = f.get(o1).toString();
				
				f = o2.getClass().getDeclaredField(sortField);
				f.setAccessible(true);
				String oo2 = f.get(o2).toString();
				
				return sortType.equalsIgnoreCase("asc") ? oo1.compareTo(oo2)  : oo2.compareTo(oo1); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		return 1;
    	} );
//		Field[]  l = listKH.get(0).getClass().getDeclaredFields();
//		for(Field f : l) {
//			String q = f.getName();
//			f.setAccessible(true);
//			String h = f.get(listKH.get(0)).toString();
//			System.out.println(h);
//		}
//		Field f = listKH.get(0).getClass().getDeclaredField("id");
//		f.setAccessible(true);
//		String d = f.get(listKH.get(0)).toString();
		
	}
}
