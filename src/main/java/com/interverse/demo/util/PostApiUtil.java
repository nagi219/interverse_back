package com.interverse.demo.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interverse.demo.dto.LinePayDTO;

@Component
public class PostApiUtil {
	
	
	public  JsonNode sendPost(LinePayDTO dto) {
		
		RestTemplate restTemplate = new RestTemplate();
		//Post.headers設定
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-LINE-ChannelId", dto.getChannelId());
		headers.add("X-LINE-Authorization", dto.getSignature());
		headers.add("X-LINE-Authorization-Nonce", dto.getNonce());
		
		
		HttpEntity<String> request = new HttpEntity<String>(dto.getBody(),headers);
		
		
		String responsebody = restTemplate.postForObject(dto.getRequestHttpUri(), request, String.class);
		
		ObjectMapper mpper = new ObjectMapper();
		
		JsonNode json = null;
		
		try {
			json = mpper.readTree(responsebody);
			return json;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return json;
		
	}
	
}
