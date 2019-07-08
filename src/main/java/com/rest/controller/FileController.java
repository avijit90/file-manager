package com.rest.controller;

import com.rest.service.FileService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.apache.logging.log4j.util.Strings.isNotEmpty;

@RestController
@RequestMapping(path = "/file")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping(value = "/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam(required = false) String encodedFileStoragePath) {
        String storageLocation = fileService.storeFile(file, encodedFileStoragePath);

        if (isNotEmpty(storageLocation))
            return "File upload completed, storageLocation - " + storageLocation;
        else
            return "File upload failed.";
    }

    @GetMapping(value = "/download")
    @ResponseBody
    public void downloadFile(HttpServletResponse response,
                             @RequestParam String encodedFileDownloadPath) {

        File fileToDownload = fileService.readFileFromPath(encodedFileDownloadPath);

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

    private void addResponseHeadersToAllowFileDownload(HttpServletResponse response, File fileToDownload) {
        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileToDownload.getName());
    }
}
