package com.rest.controller;

import com.rest.service.FileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

import static com.rest.controller.FileController.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class FileControllerTest {

    @Mock
    FileService fileService;

    @InjectMocks
    FileController unit;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void givenValidStorageLocationByFileServiceThenShouldReturnSuccessResponse() {
        MultipartFile file = mock(MultipartFile.class);
        String path = "temp/path";
        String validStorageLocation = "valid/storage/location";

        when(fileService.storeFile(file, path)).thenReturn(validStorageLocation);

        String response = unit.uploadFile(file, path);

        verify(fileService).storeFile(eq(file), eq(path));

        assertNotNull(response);
        assertTrue(response.contains(validStorageLocation));
        assertTrue(response.contains("successful"));
    }

    @Test
    public void givenInvalidStorageLocationByFileServiceThenShouldReturnFailureResponse() {
        MultipartFile file = mock(MultipartFile.class);
        String path = "temp/path";
        String inValidStorageLocation = null;

        when(fileService.storeFile(file, path)).thenReturn(inValidStorageLocation);

        String response = unit.uploadFile(file, path);

        verify(fileService).storeFile(eq(file), eq(path));

        assertNotNull(response);
        assertTrue(response.contains("failed"));
    }

    @Test
    public void givenValidFilePathThenShouldDownload() throws IOException {

        HttpServletResponse response = mock(HttpServletResponse.class);
        String validFilePath = "./backend/test/resources/Test.txt";
        File file = new File(validFilePath);

        when(fileService.readFileFromPath(eq(validFilePath))).thenReturn(file);
        ServletOutputStream servletOutputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        String actualResponse = unit.downloadFile(response, validFilePath);
        assertNotNull(actualResponse);
        verify(fileService).readFileFromPath(eq(validFilePath));
        verify(response).getOutputStream();
        verify(response).setContentType(APPLICATION_FORCE_DOWNLOAD);
        verify(response).setHeader(any(), any());
        assertEquals(actualResponse, DOWNLOAD_SUCCESSFUL);
    }

    @Test
    public void givenInValidFilePathThenDownloadShouldFail() throws IOException {

        HttpServletResponse response = mock(HttpServletResponse.class);
        String invalidFilePath = "invalid/path";

        ServletOutputStream servletOutputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        String actualResponse = unit.downloadFile(response, invalidFilePath);
        assertNotNull(actualResponse);
        verify(fileService).readFileFromPath(eq(invalidFilePath));
        verifyZeroInteractions(response);
        assertEquals(actualResponse, DOWNLOAD_FAILED);
    }


}


