package com.aibees.service.maria.common.advice;

import com.aibees.service.maria.common.domain.entity.ErrorEntity;
import com.aibees.service.maria.common.domain.entity.ResponseData;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class CommonControllerAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {

        if (body instanceof ErrorEntity) {
            return fail((ErrorEntity) body);
        } else {
            return success(body);
        }
    }

    private <T> ResponseData<T> success(T response) {
        return new ResponseData<> (true, response, null);
    }

    private <T> ResponseData<T> fail(ErrorEntity e) {
        return new ResponseData<>(false, null, e);
    }
}
