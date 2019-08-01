package com.rest.controller;

import com.rest.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.apache.logging.log4j.util.Strings.isNotEmpty;
import static org.apache.tomcat.util.http.fileupload.IOUtils.copy;

@RestController
@RequestMapping(path = "/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping(value = "/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam String path) {
        String storageLocation = fileService.storeFile(file, path);

        if (isNotEmpty(storageLocation))
            return "File upload successful, storageLocation - " + storageLocation;
        else
            return "File upload failed.";
    }

    @GetMapping(value = "/download")
    @ResponseBody
    public String downloadFile(HttpServletResponse response,
                               @RequestParam String path) {

        File fileToDownload = fileService.readFileFromPath(path);

        try {
            InputStream inputStream = new FileInputStream(fileToDownload);
            copy(inputStream, response.getOutputStream());
            addResponseHeadersToAllowFileDownload(response, fileToDownload);
            response.flushBuffer();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "Download failed.";
        }

        return "Download successful.";

    }

    private void addResponseHeadersToAllowFileDownload(HttpServletResponse response, File fileToDownload) {
        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileToDownload.getName());
    }
}
