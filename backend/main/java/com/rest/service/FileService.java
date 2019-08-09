package com.rest.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Base64;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.io.FileUtils.getFile;
import static org.apache.logging.log4j.util.Strings.isNotEmpty;
import static org.springframework.util.StringUtils.cleanPath;

@Service
public class FileService {

    private final String defaultFileStorageLocation = "C:\\";

    private String getDecodedFilePath(String encodedPath) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedPath);
        String filePath = new String(decodedBytes);
        System.out.println("decoded path is :" + filePath);
        return filePath;
    }

    public File readFileFromPath(String encodedFilePath) {
        String decodedFilePath = getDecodedFilePath(encodedFilePath);
        return getFile(decodedFilePath);
    }

    public String storeFile(MultipartFile file, String encodedFileStoragePath) {

        String fileName = cleanPath(file.getName());

        try {
            String fileStoragePath = getFileStoragePath(encodedFileStoragePath);

            Path targetLocation = get(isNotEmpty(fileStoragePath) ? fileStoragePath : defaultFileStorageLocation)
                    .toAbsolutePath().normalize();

            copy(file.getInputStream(), targetLocation.resolve(fileName), REPLACE_EXISTING);
            return targetLocation.toString();

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String getFileStoragePath(String encodedFileStoragePath) {
        if (isNotEmpty(encodedFileStoragePath))
            return getDecodedFilePath(encodedFileStoragePath);
        return null;
    }

}
