package com.example.trustcare.service;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

public class S3Uploader {

    private static final String BUCKET_NAME = "trustcare-cnic-uploads";
    private static final Region REGION = Region.AP_SOUTH_1;

    private final S3Client s3Client;

    public S3Uploader() {
        this.s3Client = S3Client.builder()
                .region(REGION)
                .credentialsProvider(ProfileCredentialsProvider.create("default")) // or use env vars
                .build();
    }
    public String uploadCNIC(int caregiverId, String filePath) throws IOException {
        String key = "cnic/caregiver_" + caregiverId + "_" + UUID.randomUUID() + ".jpg";

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .acl("public-read")
                .contentType("image/jpeg")
                .build();

        s3Client.putObject(request, RequestBody.fromFile(Paths.get(filePath)));

        return "https://" + BUCKET_NAME + ".s3." + REGION + ".amazonaws.com/" + key;
    }

}
