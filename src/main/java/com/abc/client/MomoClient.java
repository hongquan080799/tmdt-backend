package com.abc.client;

import com.abc.response.GoogleLoginResponse;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class MomoClient {
    public static Object getPayUrl(Object body){
        String momoUrl = "https://test-payment.momo.vn/v2/gateway/api/create";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(new MediaType[] {MediaType.APPLICATION_JSON}));

        HttpEntity<?> entity = new HttpEntity<>(body, headers);

        ResponseEntity<?> result = restTemplate.exchange(momoUrl, HttpMethod.POST,entity,Object.class);

        if(result.getStatusCode() == HttpStatus.OK)
            System.out.println(result.getBody());

        return result.getBody();
    }
}
