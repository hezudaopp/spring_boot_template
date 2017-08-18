package com.youmayon.tutorial.web;

import com.youmayon.tutorial.util.UuidUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Value("${server.port}")
    private Integer serverPort;


    @RequestMapping("/uuid")
    public long uuid() {
        return UuidUtils.generateUuid(1L);
    }

    @RequestMapping("/server_port")
    public Integer serverPort() {
        return serverPort;
    }

    @RequestMapping("/exception")
    public String exception() throws Exception {
        throw new Exception("Global exception.");
    }
}