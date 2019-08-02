package com.rest.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/hello")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HelloController {

    public static final String CURRENT_TIME_AT_SERVER_STRING = "Current time at server : ";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss.SSS";

    @GetMapping(path = "", produces = APPLICATION_JSON_VALUE)
    public String getHello() {
        return "{\"response\": {\"text\": \"Hello World !\"}}";
    }

    @GetMapping("/currentTime")
    public String getCurrentTime() {
        String formattedDate = new SimpleDateFormat(DATE_TIME_FORMAT).format(new Date());
        return CURRENT_TIME_AT_SERVER_STRING + formattedDate;
    }
}
