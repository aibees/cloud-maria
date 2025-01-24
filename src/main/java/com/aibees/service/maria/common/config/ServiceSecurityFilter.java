package com.aibees.service.maria.common.config;

import com.aibees.service.maria.common.excepts.MariaException;
import com.aibees.service.maria.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Component
public class ServiceSecurityFilter implements Filter {

    @Value("${servicekey.list}")
    private List<String> serviceKeyList;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String serviceKey = httpRequest.getHeader("servicekey");

        if (StringUtils.isNotEquals(httpRequest.getMethod(), "OPTIONS") && !serviceKeyList.contains(serviceKey)) {
            throw new MariaException("서비스키 오류");
        }

        chain.doFilter(request, response);
    }
}
