package com.marketplace.storage.service.impl;

import com.marketplace.api.exception.InvalidFileException;
import com.marketplace.api.exception.NotFoundException;
import com.marketplace.api.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageServiceImpl implements StorageService {

    private final S3Client s3Client;

    @Value("${app.storage.bucket-name}")
    private String bucketName;

    /**
     * Uploads a file to S3 storage with a unique key.
     * Validates file type and size before uploading.
     *
     * @param filename    Original filename
     * @param content     File content as byte array
     * @param contentType MIME type (e.g., "image/jpeg", "application/pdf")
     * @return Generated unique key (UUID_filename format)
     */
    @Override
    public String uploadFile(String filename, byte[] content, String contentType) {
        log.atDebug().log("Enter uploadFile({}, {}, {})", filename, content.length, contentType);

        validateFileType(contentType);
        validateFileSize(content.length, contentType);

        String key = UUID.randomUUID() + "_" + filename;

        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(contentType)
                        .build(),
                RequestBody.fromBytes(content));

        log.atDebug().log("Leave uploadFile() - return {}", key);

        return key;
    }

    /**
     * Downloads a file from S3 storage by its key.
     *
     * @param key Unique file identifier
     * @return File content as byte array
     * @throws NotFoundException if file not found
     */
    @Override
    public byte[] downloadFile(String key) {
        try {
            log.atDebug().log("Enter downloadFile({})", key);

            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            ResponseBytes<GetObjectResponse> response = s3Client.getObjectAsBytes(request);

            log.atDebug().log("Leave downloadFile() - return byte array");

            return response.asByteArray();
        } catch (NoSuchKeyException e) {
            log.atError().log("File with key '{}' not found", key);
            throw new NotFoundException("Le fichier avec la clé '" + key + "' n'existe pas");
        }
    }

    /**
     * Deletes a file from S3 storage by its key.
     *
     * @param key Unique file identifier
     */
    @Override
    public void deleteFile(String key) {
        log.atDebug().log("Enter deleteFile({})", key);

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());

        log.atDebug().log("Leave deleteFile()");
    }

    /**
     * Validates if file type is allowed.
     * Allowed types: JPEG, PNG, WebP, PDF.
     *
     * @param contentType MIME type to validate
     * @throws InvalidFileException if file type not allowed
     */
    private void validateFileType(String contentType) {
        log.atDebug().log("Enter validateFileType({})", contentType);

        Set<String> allowedImageTypes = Set.of(
                "image/jpeg", "image/png", "image/webp"
        );

        Set<String> allowedDocumentTypes = Set.of(
                "application/pdf"
        );

        Set<String> allowedTypes = new HashSet<>();
        allowedTypes.addAll(allowedImageTypes);
        allowedTypes.addAll(allowedDocumentTypes);

        if (contentType == null || !allowedTypes.contains(contentType.toLowerCase())) {
            log.atError().log("Invalid file type: {}", contentType);
            throw new InvalidFileException("Type de fichier non autorisé: " + contentType);
        }

        log.atDebug().log("Leave validateFileType()");
    }

    /**
     * Validates file size based on content type.
     * Limits: 2MB for images, 5MB for documents.
     *
     * @param size        File size in bytes
     * @param contentType MIME type
     * @throws IllegalArgumentException if size exceeds limit or type not supported
     */
    private void validateFileSize(int size, String contentType) {
        log.atDebug().log("Enter validateFileSize({}, {})", size, contentType);

        Map<String, Integer> maxSizes = Map.of(
                "image", 2 * 1024 * 1024,       // 2 MB for images
                "application", 5 * 1024 * 1024   // 5 MB for documents
        );

        String[] mainType = contentType.split("/");

        int maxSize = maxSizes.get(mainType[0]);

        if (size > maxSize) {
            log.atError().log("File size too large. Maximum for {}: {} MB", mainType[1], maxSize / (1024 * 1024));
            throw new InvalidFileException("Taille du fichier trop importante. Maximum pour " + mainType[1] + ": " + (maxSize / (1024 * 1024)) + " MB");
        }

        log.atDebug().log("Leave validateFileSize()");
    }
}
