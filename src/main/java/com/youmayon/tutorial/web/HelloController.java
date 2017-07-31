package com.youmayon.tutorial.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${logging.level.root}")
    private String loggingLevel;

    @RequestMapping(method = RequestMethod.GET, value = "/hello")
    public String index() {
        return "你好";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/exception")
    public String exception() throws Exception {
        throw new Exception("Global exception.");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/logging_level")
    public String logging() {
        logger.trace("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
        return loggingLevel;
    }
}