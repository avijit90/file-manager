package com.rest.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/hello")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HelloController {

    @GetMapping(path = "", produces = APPLICATION_JSON_VALUE)
    public String getHello() {
        return "{\"response\": {\"text\": \"Hello World !\"}}";
    }
}
