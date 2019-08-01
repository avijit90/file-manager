package com.rest.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static java.text.MessageFormat.format;

@Component
@Order(1)
public class RequesterLoggingFilter implements Filter {

    public static final String DESTROYING_REQUEST_LOGGING_FILTER = "Destroying request logging filter.";
    public static final String REQUEST_LOGGING_FILTER_STARTED = "Request Logging filter started successfully";

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        System.out.println("---------------------------------");

        System.out.println(format("Incoming request, requester_details={0} and request_url={1}",
                request.getRemoteAddr(), ((HttpServletRequest) request).getRequestURI()));

        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
        System.out.println(DESTROYING_REQUEST_LOGGING_FILTER);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println(REQUEST_LOGGING_FILTER_STARTED);
    }

}