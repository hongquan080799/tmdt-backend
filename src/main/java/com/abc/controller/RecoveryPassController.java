package com.abc.controller;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abc.entity.Khachhang;
import com.abc.entity.ResetPassword;
import com.abc.entity.Taikhoan;
import com.abc.repository.ResetPasswordRepository;
import com.abc.repository.TaikhoanRepository;
import com.abc.request.RestorePass;
import com.abc.request.RestorePassRequest;
import com.abc.service.MailService;

@RestController
@CrossOrigin
public class RecoveryPassController {
	
	@Autowired
	TaikhoanRepository taikhoanRepository;
	
	@Autowired
	ResetPasswordRepository resetPasswordRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	MailService mailService;
	@GetMapping("/recovery/send")
	public ResponseEntity<Boolean> getRecovery(@RequestParam("username") String username) throws Exception{
		Optional<Taikhoan> optional = taikhoanRepository.findById(username);
		if(optional.isEmpty())
			throw new Exception("Account not exits");
		Taikhoan tk = optional.get();
		if(tk.getQuyen().getMaquyen() != 2)
			throw new Exception("Permission denine !");
		Khachhang kh = tk.getListKH().get(0);
		int code = (int) (System.currentTimeMillis() / 100000);
		ResetPassword resetPassword = new ResetPassword();
		resetPassword.setCode(code);
		resetPassword.setUsername(username);
		resetPassword.setTime(new Date());
		resetPassword.setValidate(240 * 1000);
		resetPasswordRepository.save(resetPassword);
		String message = "Your verification code is " + code;
		String subject = "Recovery Your Account";
		try {
			mailService.sendEmail(kh.getEmail(), message, subject);
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/recovery/submit")
	public ResponseEntity<Boolean> submitRestore(@Validated @RequestBody RestorePassRequest request) throws Exception{
		ResetPassword resetPassword = resetPasswordRepository.findById(request.getUsername()).get();
		Long time = System.currentTimeMillis() - resetPassword.getTime().getTime();
		if(time > resetPassword.getValidate())
			throw new Exception("Your code has exprire !!");
		if(resetPassword.getCode() == request.getCode()) {
			Taikhoan tk = taikhoanRepository.findByUsername(request.getUsername());
			tk.setPassword(passwordEncoder.encode(request.getPassword()));
			try {
				taikhoanRepository.save(tk);
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
