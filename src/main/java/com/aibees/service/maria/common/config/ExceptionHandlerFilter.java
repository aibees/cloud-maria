package com.aibees.service.maria.common.config;

import com.aibees.service.maria.common.domain.entity.ErrorEntity;
import com.aibees.service.maria.common.excepts.MariaException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (MariaException me) {
            setErrorResp((HttpServletResponse) response, me);
        }
    }

    private void setErrorResp(HttpServletResponse response, MariaException me) throws IOException {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        ErrorEntity error = new ErrorEntity();
        error.setStatus("fail");
        error.setMessage(me.getMessage());

        response.getWriter().write(String.valueOf(error));
    }
}
