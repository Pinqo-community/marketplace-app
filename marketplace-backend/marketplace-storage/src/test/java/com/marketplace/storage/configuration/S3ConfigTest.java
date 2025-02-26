package com.marketplace.storage.configuration;

import io.awspring.cloud.s3.S3Exception;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ConfigTest {
    private S3Config s3Config;

    @Mock
    private S3Client s3Client;

    @Mock
    private S3ClientBuilder s3ClientBuilder;

    private final String testEndpoint = "http://localhost:4566";
    private final String testAccessKey = "test-access-key";
    private final String testSecretKey = "test-secret-key";
    private final String testRegion = "eu-west-1";
    private final String testBucketName = "test-bucket";
    private final boolean testPathStyleEnabled = true;

    @BeforeEach
    public void setup() {
        s3Config = new S3Config();
        ReflectionTestUtils.setField(s3Config, "bucketName", testBucketName);
    }

    @Test
    public void testS3ClientCreationSuccess() {
        try (MockedStatic<S3Client> s3ClientMockedStatic = mockStatic(S3Client.class);
             MockedStatic<S3Configuration> s3ConfigurationMockedStatic = mockStatic(S3Configuration.class)) {

            // Given
            S3Configuration.Builder configBuilder = mock(S3Configuration.Builder.class);
            S3Configuration s3Configuration = mock(S3Configuration.class);
            s3ConfigurationMockedStatic.when(S3Configuration::builder).thenReturn(configBuilder);
            when(configBuilder.pathStyleAccessEnabled(testPathStyleEnabled)).thenReturn(configBuilder);
            when(configBuilder.build()).thenReturn(s3Configuration);

            s3ClientMockedStatic.when(S3Client::builder).thenReturn(s3ClientBuilder);
            when(s3ClientBuilder.region(any(Region.class))).thenReturn(s3ClientBuilder);
            when(s3ClientBuilder.endpointOverride(any(URI.class))).thenReturn(s3ClientBuilder);
            when(s3ClientBuilder.credentialsProvider(any())).thenReturn(s3ClientBuilder);
            when(s3ClientBuilder.serviceConfiguration(any(S3Configuration.class))).thenReturn(s3ClientBuilder);
            when(s3ClientBuilder.build()).thenReturn(s3Client);

            when(s3Client.headBucket(any(HeadBucketRequest.class))).thenReturn(null);

            // When
            S3Client result = s3Config.s3Client(
                    testEndpoint,
                    testAccessKey,
                    testSecretKey,
                    testRegion,
                    testPathStyleEnabled
            );

            // Then
            assertSame(s3Client, result);
            verify(s3Client).headBucket(any(HeadBucketRequest.class));
            verify(s3Client, never()).createBucket(any(CreateBucketRequest.class));
            verify(s3ClientBuilder).region(Region.of(testRegion));
            verify(s3ClientBuilder).endpointOverride(URI.create(testEndpoint));
            verify(s3ClientBuilder).serviceConfiguration(s3Configuration);
        }
    }

    @Test
    public void testBucketDoesNotExistAndIsCreated() {
        try (MockedStatic<S3Client> s3ClientMockedStatic = mockStatic(S3Client.class)) {
            // Given
            s3ClientMockedStatic.when(S3Client::builder).thenReturn(s3ClientBuilder);
            when(s3ClientBuilder.region(any(Region.class))).thenReturn(s3ClientBuilder);
            when(s3ClientBuilder.endpointOverride(any(URI.class))).thenReturn(s3ClientBuilder);
            when(s3ClientBuilder.credentialsProvider(any())).thenReturn(s3ClientBuilder);
            when(s3ClientBuilder.serviceConfiguration(any(S3Configuration.class))).thenReturn(s3ClientBuilder);
            when(s3ClientBuilder.build()).thenReturn(s3Client);

            NoSuchBucketException noSuchBucketException = NoSuchBucketException.builder().build();
            doThrow(noSuchBucketException).when(s3Client).headBucket(any(HeadBucketRequest.class));
            when(s3Client.createBucket(any(CreateBucketRequest.class))).thenReturn(CreateBucketResponse.builder().build());

            // When
            S3Client result = s3Config.s3Client(
                    testEndpoint,
                    testAccessKey,
                    testSecretKey,
                    testRegion,
                    testPathStyleEnabled
            );

            // Then
            assertSame(s3Client, result);
            verify(s3Client).headBucket(any(HeadBucketRequest.class));
            verify(s3Client).createBucket(any(CreateBucketRequest.class));
        }
    }

    @Test
    public void testBucketInitializationFailsWithS3Exception() {
        try (MockedStatic<S3Client> s3ClientMockedStatic = mockStatic(S3Client.class)) {
            // Given
            s3ClientMockedStatic.when(S3Client::builder).thenReturn(s3ClientBuilder);
            when(s3ClientBuilder.region(any(Region.class))).thenReturn(s3ClientBuilder);
            when(s3ClientBuilder.endpointOverride(any(URI.class))).thenReturn(s3ClientBuilder);
            when(s3ClientBuilder.credentialsProvider(any())).thenReturn(s3ClientBuilder);
            when(s3ClientBuilder.serviceConfiguration(any(S3Configuration.class))).thenReturn(s3ClientBuilder);
            when(s3ClientBuilder.build()).thenReturn(s3Client);

            S3Exception s3Exception = mock(S3Exception.class);
            when(s3Exception.getMessage()).thenReturn("Access denied");
            doThrow(s3Exception).when(s3Client).headBucket(any(HeadBucketRequest.class));

            // When & Then
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                s3Config.s3Client(
                        testEndpoint,
                        testAccessKey,
                        testSecretKey,
                        testRegion,
                        testPathStyleEnabled
                );
            });

            // Then
            assertEquals("Failed to initialize storage bucket", exception.getMessage());
            assertEquals(s3Exception, exception.getCause());
            verify(s3Client).headBucket(any(HeadBucketRequest.class));
            verify(s3Client, never()).createBucket(any(CreateBucketRequest.class));
        }
    }
}