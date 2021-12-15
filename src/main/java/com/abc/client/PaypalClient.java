package com.abc.client;

import com.abc.request.PayPalResponse;
import com.abc.response.PaypalRefund;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaypalClient {
    @Autowired
    RedisTemplate redisTemplate;

    public static String paypalToken = "";
    public PaypalClient(){
        if(paypalToken.equalsIgnoreCase(""))
            paypalToken = getAccessToken();
    }


    public String getPayLink(String redirectUrl, Float price, String username){
        String token = "Bearer " + paypalToken;
        String url = "https://api-m.sandbox.paypal.com/v1/payments/payment";
        JSONObject body = new JSONObject();
        Gson gson = new Gson();
        RestTemplate restTemplate = new RestTemplate();

        body.put("intent","sale");
        body.put("payer", new JSONObject().put("payment_method", "paypal"));
        body.put("transactions", new JSONObject[]{
                new JSONObject().put("amount",
                        new JSONObject()
                                .put("total",price)
                                .put("currency","USD"))
        });
        body.put("redirect_urls", new JSONObject()
                .put("return_url",redirectUrl)
                .put("cancel_url",redirectUrl));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);
        headers.setAccept(Arrays.asList(new MediaType[] {MediaType.APPLICATION_JSON}));
        Object bodyRequest = gson.fromJson(body.toString(), Object.class);
        HttpEntity<Object> entity = new HttpEntity<>(bodyRequest, headers);
        ResponseEntity<PayPalResponse> result = restTemplate.exchange(url, HttpMethod.POST,entity,PayPalResponse.class);
        if(result.getStatusCode() == HttpStatus.CREATED){
           PayPalResponse res = result.getBody();
           redisTemplate.opsForValue().set(username + "_confirm", res.getLinks().get(2).getHref());
           return res.getLinks().get(1).getHref();
        }

        return "";
    }

    public String getAccessToken(){
        String url = "https://api-m.sandbox.paypal.com/v1/oauth2/token";
        JSONObject body = new JSONObject();
        Gson gson = new Gson();
        RestTemplate restTemplate = new RestTemplate();
//        String plainCreds = "Ac901l4crVLWnDaQvqaR_Js8yVR3L65kyb4ABuJ_WpOHx1LpJ5ev3EDK5DWxoXX_K2XkP2Duwy8n3urV:EPty5NzLd82fTm83pspbiYGTfjTKjLdkxh8thmW9LQhcLLY-Tr1_eYhtzuilRBpd6A0g_g4HTNGzH3JH";
//        byte[] plainCredsBytes = plainCreds.getBytes();
//        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
//        String base64Creds = new String(base64CredsBytes);
        //body.put("grant_type","client_credentials");
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept","application/x-www-form-urlencoded");
        headers.setBasicAuth("Ac901l4crVLWnDaQvqaR_Js8yVR3L65kyb4ABuJ_WpOHx1LpJ5ev3EDK5DWxoXX_K2XkP2Duwy8n3urV",
                "EPty5NzLd82fTm83pspbiYGTfjTKjLdkxh8thmW9LQhcLLY-Tr1_eYhtzuilRBpd6A0g_g4HTNGzH3JH");
        headers.setAccept(Arrays.asList(new MediaType[] {MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED}));
        Object bodyRequest = gson.fromJson(body.toString(), Object.class);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<?> result = restTemplate.exchange(url, HttpMethod.POST,entity,Object.class);
        if(result.getStatusCode() == HttpStatus.OK){
            return new JSONObject(gson.toJson(result.getBody())).getString("access_token");
        }
        return "";
    }
    public String submitOrder (String username, String payerId){
        String key = username + "_confirm";
        if(key.isEmpty() || key.isBlank())
            return "";
        String url = (String) redisTemplate.opsForValue().get(key);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[] {MediaType.APPLICATION_JSON}));
        headers.set("Authorization", "Bearer " + paypalToken);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        Map<String, String> body = new HashMap<>();
        body.put("payer_id", payerId);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        ResponseEntity<PaypalRefund> result = restTemplate.exchange(url, HttpMethod.POST, entity, PaypalRefund.class);
        if(result.getStatusCode() == HttpStatus.OK){
            PaypalRefund paypalRefund = result.getBody();
            return paypalRefund.getTransactions().get(0).getRelated_resources().get(0).getSale().getLinks().get(1).getHref();
        }
        return "";
    }
    public Boolean cashBack (String url){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(new MediaType[] {MediaType.APPLICATION_JSON}));
        headers.set("Authorization", "Bearer " + paypalToken);
    
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        if(result.getStatusCode() == HttpStatus.CREATED){
            return true;
        }
        return false;
    }

}
