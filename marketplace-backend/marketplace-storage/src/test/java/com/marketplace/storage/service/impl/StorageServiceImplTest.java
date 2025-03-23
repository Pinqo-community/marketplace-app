package com.marketplace.storage.service.impl;

import com.marketplace.api.exception.InvalidFileException;
import com.marketplace.api.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StorageServiceImplTest {
    @Mock
    private S3Client s3Client;

    @InjectMocks
    private StorageServiceImpl storageService;

    private final String testBucketName = "test-bucket";
    private final String validImageContentType = "image/jpeg";
    private final String validDocumentContentType = "application/pdf";
    private final String invalidContentType = "text/plain";
    private final byte[] smallImageContent = new byte[1024 * 1024]; // 1MB
    private final byte[] largeDocumentContent = new byte[6 * 1024 * 1024]; // 6MB

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(storageService, "bucketName", testBucketName);
    }

    @Nested
    class UploadFileTests {

        @Test
        void testUploadFile_WithValidImageFile_ShouldSucceed() {
            // Given
            String filename = "test-image.jpg";

            // When
            String result = storageService.uploadFile(filename, smallImageContent, validImageContentType);

            // Then
            assertNotNull(result);
            assertTrue(result.endsWith("_" + filename));
            verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        }

        @Test
        void testUploadFile_WithInvalidFileType_ShouldThrowInvalidFileException() {
            // Given
            String filename = "test-file.txt";

            // When & Then
            InvalidFileException exception = assertThrows(
                    InvalidFileException.class,
                    () -> storageService.uploadFile(filename, smallImageContent, invalidContentType)
            );

            verify(s3Client, never()).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        }

        @Test
        void testUploadFile_WithContentTypeNull_ShouldThrowInvalidFileException() {
            // Given
            String filename = "test-file.txt";

            // When & Then
            InvalidFileException exception = assertThrows(
                    InvalidFileException.class,
                    () -> storageService.uploadFile(filename, smallImageContent, null)
            );

            verify(s3Client, never()).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        }

        @Test
        void testUploadFile_WithTooLargeDocumentFile_ShouldThrowIllegalArgumentException() {
            // Given
            String filename = "large-document.pdf";

            // When & Then
            InvalidFileException exception = assertThrows(
                    InvalidFileException.class,
                    () -> storageService.uploadFile(filename, largeDocumentContent, validDocumentContentType)
            );

            verify(s3Client, never()).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        }
    }

    @Nested
    class DownloadFileTests {

        @Test
        void testDownloadFile_WithExistingFile_ShouldReturnContent() {
            // Given
            String key = "existing-file-key";
            byte[] fileContent = "file content".getBytes();

            ResponseBytes<GetObjectResponse> responseBytes = mock(ResponseBytes.class);
            when(responseBytes.asByteArray()).thenReturn(fileContent);
            when(s3Client.getObjectAsBytes(any(GetObjectRequest.class))).thenReturn(responseBytes);

            // When
            byte[] result = storageService.downloadFile(key);

            // Then
            assertNotNull(result);
            assertArrayEquals(fileContent, result);
            verify(s3Client).getObjectAsBytes(any(GetObjectRequest.class));
        }

        @Test
        void testDownloadFile_WithNonExistingFile_ShouldThrowNotFoundException() {
            // Given
            String key = "non-existing-file-key";
            when(s3Client.getObjectAsBytes(any(GetObjectRequest.class))).thenThrow(NoSuchKeyException.class);

            // When & Then
            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> storageService.downloadFile(key)
            );

            verify(s3Client).getObjectAsBytes(any(GetObjectRequest.class));
        }
    }

    @Nested
    class DeleteFileTests {

        @Test
        void testDeleteFile_ShouldCallS3ClientDelete() {
            // Given
            String key = "file-to-delete";

            // When
            storageService.deleteFile(key);

            // Then
            verify(s3Client).deleteObject(any(DeleteObjectRequest.class));
        }
    }
}