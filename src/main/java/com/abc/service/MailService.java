package com.abc.service;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.abc.entity.Khachhang;
import com.abc.entity.Taikhoan;
import com.abc.repository.TaikhoanRepository;

@Service
public class MailService {
	
	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	TaikhoanRepository taikhoanRepository;
	
	public void sendVerificationMail(String to,String toName, String urlSite, String username) throws Exception{
		String subject = "Please verification your account";
		String from = "issacnewton321@gmail.com";
		String content = "<h2>Dear " + toName +"</h2>"
				+ "<h3>Please click this link below to verify your registration: "
				+ "<a href=\"[[URL]]\">Verification link</a></h3>"
				+ "<h3>Thank you</h3>"
				+ "<h3 style='color:red'>AngryBird Company</h3>";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(from, "AngryBird Company");
		helper.setTo(to);
		helper.setSubject(subject);
		
		String verifyUrl = urlSite + "/verify?code=" + getVerifyCode(username);
		content = content.replace("[[URL]]", verifyUrl );
		helper.setText(content, true);
		
		mailSender.send(message);
	}
	
	public void sendEmail(String to, String message, String subject) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(message);
		mailSender.send(msg);
	}
	
	public String getVerifyCode(String username) {
		Taikhoan tk = taikhoanRepository.findByUsername(username);
		return tk.getVerificationCode();
	}
}
