package com.youmayon.tutorial.web;

import com.youmayon.tutorial.service.BlogProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jawinton on 25/06/2017.
 */
@RestController
public class BlogController {
    @Autowired
    private BlogProperties blogProperties;

    @RequestMapping("/blog")
    public BlogProperties getBlog() {
        return blogProperties;
    }
}
