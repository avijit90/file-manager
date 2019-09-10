package com.rest.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;

import static org.apache.commons.io.FileUtils.contentEquals;
import static org.apache.commons.io.FileUtils.getFile;
import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class FileServiceTest {

    public static final String VALID_FILE_PATH = "./backend/test/resources/Test.txt";
    public static final String VALID_STORAGE_PATH = "./backend/test/resources";
    public static final String INVALID_FILE_PATH = "test/path";

    @InjectMocks
    FileService unit;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void givenAValidEncodedFilePathThenShouldDecodeAndReadFile() throws IOException {
        String validEncodedPath = getEncodedString(VALID_FILE_PATH);

        File file = unit.readFileFromPath(validEncodedPath);

        assertNotNull(file);
        assertTrue(contentEquals(file, getFile(VALID_FILE_PATH)));
    }

    @Test
    public void givenAnInvalidEncodedFilePathThenShouldDecodeAndFail() throws IOException {
        String invalidEncodedPath = getEncodedString(INVALID_FILE_PATH);

        File file = unit.readFileFromPath(invalidEncodedPath);

        assertNotNull(file);
        assertFalse(file.exists());
    }

    @Test
    public void givenAValidMultipartFileAndEncodedPathThenShouldStoreFile() {

        try {
            String newFileName = "newFileName.txt";
            MultipartFile multipartFile = getMultipartFileFromPath(VALID_FILE_PATH, newFileName);
            String encodedPath = getEncodedString(VALID_STORAGE_PATH);

            String response = unit.storeFile(multipartFile, encodedPath);

            assertNotNull(response);
            File newlyCreatedFile = getFile(response + "\\" + newFileName);

            System.out.println("Existing file contents :");
            try (BufferedReader br = new BufferedReader(new FileReader(VALID_FILE_PATH))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }
            System.out.println("-----------------------");
            System.out.println("New file contents :");
            try (BufferedReader br = new BufferedReader(new FileReader(response + "\\" + newFileName))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }

            assertTrue(FileUtils.contentEquals(FileUtils.getFile(VALID_FILE_PATH), newlyCreatedFile));
            FileUtils.deleteQuietly(newlyCreatedFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private MultipartFile getMultipartFileFromPath(String path, String newFileName) throws Exception {
        File file = new File(path);
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(newFileName,
                file.getName(), "text/plain", IOUtils.toByteArray(input));
        input.close();
        return multipartFile;
    }

    private String getEncodedString(String validFilePath) {
        return Base64.getEncoder().encodeToString(validFilePath.getBytes());
    }

}
