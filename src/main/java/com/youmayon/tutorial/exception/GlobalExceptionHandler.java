package com.youmayon.tutorial.exception;

import com.youmayon.tutorial.dto.DefaultResponseInfo;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public DefaultResponseInfo<String> defaultErrorHandler(HttpServletRequest request, Exception e, HttpServletResponse response) throws Exception {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new DefaultResponseInfo<>(500000, e.getMessage(), request.getRequestURL().toString(), "");
    }

    @ExceptionHandler(value = InvalidRequestException.class)
    @ResponseBody
    public DefaultResponseInfo<String> jsonErrorHandler(HttpServletRequest request, InvalidRequestException e, HttpServletResponse response) throws Exception {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return new DefaultResponseInfo<>(400001, e.getMessage(), request.getRequestURL().toString(), "");
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public DefaultResponseInfo<String> jsonErrorHandler(HttpServletRequest request, IllegalArgumentException e, HttpServletResponse response) throws Exception {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return new DefaultResponseInfo<>(400002, e.getMessage(), request.getRequestURL().toString(), "");
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseBody
    public DefaultResponseInfo<String> jsonErrorHandler(HttpServletRequest request, UserNotFoundException e, HttpServletResponse response) throws Exception {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return new DefaultResponseInfo<>(404001, e.getMessage(), request.getRequestURL().toString(), "");
    }
}

