package com.abc.jwt.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.abc.entity.Taikhoan;
import com.abc.repository.TaikhoanRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	TaikhoanRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Taikhoan taikhoan = repo.findByUsernameAndStatus(username, 1);
		if(taikhoan == null) {
			throw new UsernameNotFoundException(username);
		}
		return new CustomUserDetail(taikhoan);
	}

}
