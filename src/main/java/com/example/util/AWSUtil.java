package com.example.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AWSUtil {
	
	@Value("${aws.region}")
	private String awsRegion;
	
	@Value("${aws.bucketName}")
	private String bucketName;
	
	@Value("${aws.credentialPath}")
	private String path;
	
	@Value("${aws.stagingDir}")
	private String stagingDir;
	
	@Value("${aws.filePath}")
	private String filePath;

	public AmazonS3 getConnection(AmazonS3 s3) throws Exception {
		log.info("inside AWSConfig getConnection() method");

		String content = null;
		try {
			content = new String(Files.readAllBytes(Paths.get(path)));
		} catch (IOException ie) {
			log.info("Error reading file - "+ie.getMessage());
		}

		String[] con = content.split("\n");

		String accessKeyId = con[1].split(" = ")[1];
		String secretKey = con[2].split(" = ")[1];
		String securityToken = con[3].split(" = ")[1];

		AWSCredentialsProvider credProv = new AWSCredentialsProvider() {

			@Override
			public void refresh() {
			}

			@Override
			public AWSCredentials getCredentials() {
				return new BasicSessionCredentials(accessKeyId, secretKey, securityToken);
			}
		};

		try {
			s3 = AmazonS3ClientBuilder.standard().withRegion(awsRegion).withCredentials(credProv).build();
		} catch (Exception e) {
			log.info("Exception occured while configuring AWS S3 - "+e.getMessage());
		}
		return s3;
	}

	public void moveEnrollmentFiles(String filePath, String fileExtension,
			String stagingFilePrefix) {

		AmazonS3 s3 = null;
		try {
			s3 = getConnection(s3);
			System.out.println("Connection Successful");
			ListObjectsV2Request req = new ListObjectsV2Request()
					.withBucketName(bucketName)
					.withPrefix(filePath)
					.withDelimiter("/");
			ListObjectsV2Result result;
			result = s3.listObjectsV2(req);
			System.out.println("Listing Objects");
			for (S3ObjectSummary objectSummary:result.getObjectSummaries()) {
				System.out.println("file name - "+objectSummary.getKey());
				String[] files = objectSummary.getKey().split("/");
				String fileName = files[files.length-1];
				System.out.println("file name modified = "+fileName);
				if (StringUtils.equalsIgnoreCase(fileExtension, FilenameUtils.getExtension(fileName))) {
					Date lastModifiedTime = objectSummary.getLastModified();
					String sourceFile = filePath + fileName;
					String stagingFileName = getStagingFileName(fileName, stagingFilePrefix);
					String destFile = stagingDir + stagingFileName;
					
					s3.copyObject(bucketName, sourceFile, bucketName, destFile);
					s3.deleteObject(bucketName, sourceFile);
				}
			}
		} catch (Exception e) {
			log.info("Exception while doing file operation - " + e.getMessage());
		} finally {
			if (s3 != null) {
				s3.shutdown();
			}
		}
	}
	
	public void send(String fileName) throws IOException {

		File file = new File(fileName);
		String fullPath = filePath + fileName;
		
		AmazonS3 s3 = null;
		try {
			s3 = getConnection(s3);
			s3.putObject(bucketName, fullPath, file);
			log.info("File transfered successfully to aws host.");
		} catch (Exception e) {
			log.info("Exception while doing file operation - " + e.getMessage());
		} finally {
			if (s3 != null) {
				s3.shutdown();
			}
		}
	}
	
	private String getStagingFileName(String fileName, String stagingFilePrefix) {
		return stagingFilePrefix + "_" + fileName;
	}
}