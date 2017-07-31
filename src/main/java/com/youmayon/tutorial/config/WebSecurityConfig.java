package com.youmayon.tutorial.config;

import com.youmayon.tutorial.constant.SecurityConstants;
import com.youmayon.tutorial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * Created by Jawinton on 26/06/2017.
 * Spring security 配置
 */
@Configuration
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // oauth server 不需要 csrf 防护
                .authorizeRequests()
//                .anyRequest().authenticated() //其他页面都需要登录后访问
                .anyRequest().permitAll()
                .and()
                .httpBasic().disable(); // 禁止 basic 认证
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(new StandardPasswordEncoder(SecurityConstants.PASSWORD_ENCODER_SECRET));
    }

    // 将 AuthenticationManager 注册为 bean , 方便配置 oauth server 的时候使用
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}