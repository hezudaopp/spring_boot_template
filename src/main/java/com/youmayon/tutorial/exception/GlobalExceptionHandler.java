package com.youmayon.tutorial.exception;

import com.youmayon.tutorial.dto.ErrorInfo;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorInfo<String> defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ErrorInfo<String> errorInfo = new ErrorInfo<>();
        errorInfo.setMessage(e.getMessage());
        errorInfo.setCode(ErrorInfo.getERROR());
        errorInfo.setData("Global exception");
        errorInfo.setUrl(req.getRequestURL().toString());
        return errorInfo;
    }

    @ExceptionHandler(value = InvalidRequestException.class)
    @ResponseBody
    public ErrorInfo<String> jsonErrorHandler(HttpServletRequest req, InvalidRequestException e) throws Exception {
        ErrorInfo<String> errorInfo = new ErrorInfo<>();
        errorInfo.setMessage(e.getMessage());
        errorInfo.setCode(ErrorInfo.getERROR());
        errorInfo.setData("Invalid request.");
        errorInfo.setUrl(req.getRequestURL().toString());
        return errorInfo;
    }

}

