package com.example.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
public class RestcallController {
	
	@Autowired
	private RestTemplate restTemplate;

	private static final String myUrl= "https://restcall.com/api/upload/bucket-name/sub-directory/file-name";
	
	private static final Logger logger = LoggerFactory.getLogger(RestcallController.class);
	
	@RequestMapping(value = "/group/census/v1/aws", method = RequestMethod.POST	)
	public String awsPut() {
		InputStream is = null;
		try {
			is = new ClassPathResource("test.csv").getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			byte[] b = is.readAllBytes();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		File file = null;
		try {
			file = new ClassPathResource("test.csv").getFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.ALL));
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		byte[] fileContent = null;
		try {
			fileContent = Files.readAllBytes(file.toPath());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ByteArrayResource resource = new ByteArrayResource(fileContent);
		HttpEntity<ByteArrayResource> requestEntity = new HttpEntity<>(resource, headers);
		try {
			ResponseEntity<ByteArrayResource> response = restTemplate.exchange(
				myUrl,
				HttpMethod.PUT,
				requestEntity,
				ByteArrayResource.class);
			return "file uploaded successfully";
		} catch (RestClientException e) {
			e.printStackTrace();
			return "file upload failed";
		}
	}
	
	@RequestMapping(value = "/group/census/v1/aws", method = RequestMethod.GET	)
	public String awsGet() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.ALL));
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);
		try {
			FileOutputStream fos = new FileOutputStream("C:\\Users\\gowtham\\Documents\\workspace-spring-tool-suite-4-4.14.0.RELEASE\\restcall\\src\\main\\resources\\data.txt");
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		
		try {
			ResponseEntity<byte[]> response = restTemplate.exchange(
				myUrl,
				HttpMethod.GET,
				requestEntity,
				byte[].class);
		//	ByteArrayResource bar = response.getBody();
			byte[] b = response.getBody();
			try {
				Files.write(Paths.get("data.txt"), b);
				System.out.println(Paths.get("data.txt"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String str = new String(b, StandardCharsets.UTF_8);
			System.out.println(str);
			System.out.println(response.getBody());
			return "file read successfully";
		} catch (RestClientException e) {
			e.printStackTrace();
			return "file read failed";
		}
	}
}

