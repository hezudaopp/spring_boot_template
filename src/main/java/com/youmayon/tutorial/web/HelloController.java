package com.youmayon.tutorial.web;

import com.youmayon.tutorial.exception.MyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Value("${server.port}")
    private Integer serverPort;


    @RequestMapping("/hello")
    public String index() {
        return "你好";
    }

    @RequestMapping("/server_port")
    public Integer serverPort() {
        return serverPort;
    }

    @RequestMapping("/my_exception")
    public String myException() throws MyException {
        throw new MyException("发生错误2");
    }

    @RequestMapping("/exception")
    public String exception() throws Exception {
        throw new Exception("Global exception.");
    }
}