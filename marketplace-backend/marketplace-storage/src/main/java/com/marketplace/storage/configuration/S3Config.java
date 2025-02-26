package com.marketplace.storage.configuration;

import io.awspring.cloud.s3.S3Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

import java.net.URI;

@Configuration
@Slf4j
public class S3Config {

    @Value("${app.storage.bucket-name}")
    private String bucketName;

    @Bean
    public S3Client s3Client(
            @Value("${spring.cloud.aws.s3.endpoint}") String endpoint,
            @Value("${spring.cloud.aws.credentials.access-key}") String accessKey,
            @Value("${spring.cloud.aws.credentials.secret-key}") String secretKey,
            @Value("${spring.cloud.aws.region.static}") String region,
            @Value("${spring.cloud.aws.s3.path-style-access-enabled:true}") boolean pathStyleAccessEnabled) {

        S3Configuration serviceConfiguration = S3Configuration.builder()
                .pathStyleAccessEnabled(pathStyleAccessEnabled)
                .build();

        S3Client s3Client = S3Client.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey))
                .serviceConfiguration(serviceConfiguration)
                .build();

        // Create the bucket if it doesn't exist
        ensureBucketExists(s3Client, bucketName);

        return s3Client;
    }

    private void ensureBucketExists(S3Client s3Client, String bucketName) {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
            log.atInfo().log("Bucket {} exists", bucketName);
        } catch (NoSuchBucketException e) {
            log.atInfo().log("Creating bucket: {}", bucketName);
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
            log.atInfo().log("Bucket {} created successfully", bucketName);
        } catch (S3Exception e) {
            log.atError().log("Error checking/creating bucket {}: {}", bucketName, e.getMessage());
            throw new RuntimeException("Failed to initialize storage bucket", e);
        }
    }
}
