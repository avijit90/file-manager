package com.rest.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
@Order(1)
public class RequesterLoggingFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        System.out.println("---------------------------------");
        System.out.println("Requester details :" + request.getRemoteAddr());

        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
        System.out.println("Destroying request logging filter.");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Request Logging filter is up !");
    }

}