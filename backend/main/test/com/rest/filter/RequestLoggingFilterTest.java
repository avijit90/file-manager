package com.rest.filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class RequestLoggingFilterTest {

    @InjectMocks
    RequesterLoggingFilter unit;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void givenValidRequestThenShouldFilter() throws IOException, ServletException {
        ServletRequest request = mock(HttpServletRequest.class);
        FilterChain chain = mock(FilterChain.class);
        ServletResponse response = mock(ServletResponse.class);

        when(request.getRemoteAddr()).thenReturn("localhost");
        when(((HttpServletRequest) request).getRequestURI()).thenReturn("test/case");

        unit.doFilter(request, response, chain);

        verify(request).getRemoteAddr();
        verify(chain).doFilter(eq(request), eq(response));
        verify((HttpServletRequest) request).getRequestURI();
    }
}
