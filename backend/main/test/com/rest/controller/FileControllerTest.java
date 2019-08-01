package com.rest.controller;

import com.rest.service.FileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
        String validStorageLocation = "temp/storage/location";

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


}


