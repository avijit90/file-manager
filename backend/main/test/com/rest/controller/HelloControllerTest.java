package com.rest.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.rest.controller.HelloController.CURRENT_TIME_AT_SERVER_STRING;
import static com.rest.controller.HelloController.DATE_TIME_FORMAT;
import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class HelloControllerTest {

    @InjectMocks
    HelloController unit;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void givenHelloServiceIsInvokedThenShouldReturnResponse() {

        String response = unit.getHello();

        assertNotNull(response);
        assertTrue(response.contains("Hello World !"));
    }

    @Test
    public void givenCurrentTimeServiceIsInvokedThenShouldReturnTimeAtServer() {

        String response = unit.getCurrentTime();

        assertNotNull(response);
        assertTrue(response.contains(CURRENT_TIME_AT_SERVER_STRING));

        compareDateFromResponse(response);

    }

    private void compareDateFromResponse(String response) {
        String[] result = response.split(CURRENT_TIME_AT_SERVER_STRING);
        try {
            Date responseDate = new SimpleDateFormat(DATE_TIME_FORMAT).parse(result[1]);
            Date currentDate = new Date();
            assertFalse(responseDate.compareTo(currentDate) < 0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
