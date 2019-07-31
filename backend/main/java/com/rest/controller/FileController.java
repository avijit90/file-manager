package com.rest.controller;

import com.rest.service.FileService;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.logging.log4j.util.Strings.isNotEmpty;

@RestController
@RequestMapping(path = "/file")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping(value = "/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam(required = false) String path) {
        String storageLocation = fileService.storeFile(file, path);

        if (isNotEmpty(storageLocation))
            return "File upload completed, storageLocation - " + storageLocation;
        else
            return "File upload failed.";
    }

    @GetMapping(value = "/download")
    @ResponseBody
    public void downloadFile(HttpServletResponse response,
                             @RequestParam String path) {

        File fileToDownload = fileService.readFileFromPath(path);

        try {
            InputStream inputStream = new FileInputStream(fileToDownload);
            IOUtils.copy(inputStream, response.getOutputStream());
            addResponseHeadersToAllowFileDownload(response, fileToDownload);
            response.flushBuffer();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @GetMapping(path = "/testDownload")
    public ResponseEntity<byte[]> getRandomFile(@RequestParam String path) {
        File fileToDownload = fileService.readFileFromPath(path);

        byte[] fileContent = null;
        try {
            fileContent = FileUtils.readFileToByteArray(fileToDownload);
        } catch (IOException e) {
            //throw new IOException("Unable to convert file to byte array. " + e.getMessage());
            e.printStackTrace();
        }

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        header.setContentLength(fileContent.length);
        header.set("Content-Disposition", "attachment; filename=" + fileToDownload.getName());
        return new ResponseEntity<>(fileContent, header, HttpStatus.OK);
    }

    private void addResponseHeadersToAllowFileDownload(HttpServletResponse response, File fileToDownload) {
        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileToDownload.getName());
    }
}
