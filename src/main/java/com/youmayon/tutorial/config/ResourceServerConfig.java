package com.youmayon.tutorial.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
 * Auth2 资源配置类
 * Created by Jawinton on 16/12/24.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * 配置资源访问权限
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/hello").permitAll() // hello接口不需要认证即可访问
                .antMatchers(HttpMethod.POST, "/users").permitAll() // 注册用户不需要认证
                .anyRequest().authenticated();
//        .anyRequest().permitAll();
        http.exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}
