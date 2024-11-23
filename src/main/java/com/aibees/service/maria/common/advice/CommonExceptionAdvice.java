package com.aibees.service.maria.common.advice;

import com.aibees.service.maria.common.domain.entity.ErrorEntity;
import com.aibees.service.maria.common.excepts.MariaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommonExceptionAdvice {

    @ExceptionHandler(MariaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorEntity handleException(MariaException me) {

        log.error(me.getMessage() + " / " + me.getCause());
        return new ErrorEntity("error", me.getMessage());
    }
}
