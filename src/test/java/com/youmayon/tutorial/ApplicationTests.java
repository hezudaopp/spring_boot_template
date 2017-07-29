package com.youmayon.tutorial;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
    private UserController userController;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(
                new HelloController(),
                userController).build();
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

    // 测试UserController
    @Test
    public void testUserController() throws Exception {
        RequestBuilder request;

        String username = "hezudaopp";
        String password = "youmayon";
        String newPassword = "youmayon1";

        // 2、post提交一个user
        String json = "{\"username\":\""+ username +"\", \"password\":\"" + password + "\"}";
        request = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        MvcResult mvcResult = mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"username\":\""+ username +"\"")))
                .andReturn();
        JSONObject jasonObject = JSON.parseObject(mvcResult.getResponse().getContentAsString());
        String id = jasonObject.get("id").toString();

//        String accessToken = getPasswordGrantTypeAccessToken(username, password);
        String accessToken = "";

        // 3、get获取user列表，应该有刚才插入的数据
        request = get("/users" + "?access_token = " + accessToken);
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"username\":\""+ username +"\"")));

        // 4、put修改id为{id}的user
        json = "{\"username\":\"newUsername\", \"password\":\"" + newPassword + "\"}";
        request = patch("/users/" + id + "?access_token = " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // 6、del删除id为{id}的user
        request = delete("/users/" + id + "?access_token = " + accessToken);
        mvc.perform(request)
                .andExpect(status().isOk());

        // 7、get查一下user列表，应该为空
        request = get("/users" + "?access_token = " + accessToken);
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[]")));

    }

    private String getPasswordGrantTypeAccessToken(String username, String password) throws Exception {
        RequestBuilder request = post("/oauth/token?grant_type=password&username=" + username + "&password=" + password);
        MvcResult mvcResult = mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"access_token\":")))
                .andReturn();
        JSONObject jasonObject = JSON.parseObject(mvcResult.getResponse().getContentAsString());
        return jasonObject.get("access_token").toString();
    }
}
