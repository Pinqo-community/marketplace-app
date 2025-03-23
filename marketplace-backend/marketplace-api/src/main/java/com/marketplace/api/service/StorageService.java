package com.marketplace.api.service;

/**
 * Service interface for managing file storage operations.
 */
public interface StorageService {
    /**
     * Uploads a file to the storage system.
     *
     * @param filename    Name of the file
     * @param content     File content as a byte array
     * @param contentType MIME type of the content (e.g., "image/jpeg", "application/pdf")
     * @return Unique identifier of the uploaded file
     */
    String uploadFile(String filename, byte[] content, String contentType);

    /**
     * Downloads a file from the storage system.
     *
     * @param key Unique identifier of the file to download
     * @return The file content as a byte array
     */
    byte[] downloadFile(String key);

    /**
     * Deletes a file from the storage system.
     *
     * @param key Unique identifier of the file to delete
     */
    void deleteFile(String key);
}
