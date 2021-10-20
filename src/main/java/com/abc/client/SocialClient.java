package com.abc.client;

import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.abc.response.FacebookLoginResponse;
import com.abc.response.GoogleLoginResponse;

public class SocialClient {
	String facebookUrl = "https://graph.facebook.com/me?fields=id,first_name,middle_name,last_name,email,picture{url}";
	String googleUrl = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token={token}";
	public FacebookLoginResponse getFacebookInfo(String token) {
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(new MediaType[] {MediaType.APPLICATION_JSON}));
		
		headers.set("Authorization","Bearer " + token);
		
		HttpEntity<FacebookLoginResponse> entity = new HttpEntity<>(headers);
		
		ResponseEntity<FacebookLoginResponse> result = restTemplate.exchange(facebookUrl, HttpMethod.GET,entity,FacebookLoginResponse.class,"{url}");
		
		if(result.getStatusCode() == HttpStatus.OK)
			return result.getBody();
		
		return null;
	}
	
	public GoogleLoginResponse getGoogleinfo(String token) {
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(new MediaType[] {MediaType.APPLICATION_JSON}));
		
		HttpEntity<GoogleLoginResponse> entity = new HttpEntity<>(headers);
		
		ResponseEntity<GoogleLoginResponse> result = restTemplate.exchange(googleUrl, HttpMethod.GET,entity,GoogleLoginResponse.class,token);
		
		if(result.getStatusCode() == HttpStatus.OK)
			return result.getBody();
		
		return null;
	}
	public static void main(String[] args) {
		 GoogleLoginResponse go = new SocialClient().getGoogleinfo("ya29.a0ARrdaM8R8thGLxZm5jLFOfjatiw0eG0XHGl9c0ifSAflpcWJkcEhYmtBaHdE6cKnTq_T-zupwjLU5HYWXaxDd6dQM9-sHK44kD--ad2u7ano5FTct5c_v5MYoYff_PB32hC6NADCQcSk_EGhgMdbUhs45kBjjec");
		 System.out.println(go.getPicture());
	}
}
