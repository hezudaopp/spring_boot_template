package com.youmayon.tutorial;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.youmayon.tutorial.web.BlogController;
import com.youmayon.tutorial.web.HelloController;
import com.youmayon.tutorial.web.UserController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ApplicationTests {
    private MockMvc mvc;

    @Autowired
    private BlogController blogController;

    @Autowired
    private UserController userController;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(
                new HelloController(),
                userController,
                blogController).build();
    }

    @Test
    public void getBlog() throws Exception {
        mvc.perform(get("/blog").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getHello() throws Exception {
        mvc.perform(get("/hello").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("你好")));
    }

    //  	测试UserController
    @Test
    public void testUserController() throws Exception {
        RequestBuilder request = null;

        // 1、get查一下user列表，应该为空
        request = get("/users/");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[]")));

        // 2、post提交一个user
        request = post("/users/")
                .param("username", "测试大师");
        MvcResult mvcResult = mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"username\":\"测试大师\"")))
                .andReturn();
        JSONObject jasonObject = JSON.parseObject(mvcResult.getResponse().getContentAsString());
        String id = jasonObject.get("id").toString();

        // 3、get获取user列表，应该有刚才插入的数据
        request = get("/users/");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"username\":\"测试大师\"")));

        // 4、put修改id为{id}的user
        request = put("/users/" + id)
                .param("username", "测试终极大师");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"username\":\"测试终极大师\"")));

        // 5、get一个id为{id}的user
        request = get("/users/" + id);
        mvc.perform(request)
                .andExpect(content().string(equalTo("{\"id\":" + id + ",\"username\":\"测试终极大师\"}")));

        // 6、del删除id为{id}的user
        request = delete("/users/" + id);
        mvc.perform(request)
                .andExpect(status().isOk());

        // 7、get查一下user列表，应该为空
        request = get("/users/");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[]")));

    }

}
