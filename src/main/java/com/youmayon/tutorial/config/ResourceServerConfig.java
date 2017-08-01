package com.youmayon.tutorial.config;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;

/**
 * Auth2 资源配置类
 * Created by Jawinton on 16/12/24.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Value("${resource.id:spring-boot-template}")
    private String resourceId;

    /**
     * 配置资源访问权限
     * @param http HttpSecurity
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/hello").permitAll() // hello接口不需要认证即可访问
                .antMatchers(HttpMethod.POST, "/users").permitAll() // 注册用户不需要认证
                .antMatchers(HttpMethod.GET, "/swagger-resources").permitAll()
                .antMatchers(HttpMethod.GET, "/v2/api-docs").permitAll()
                .antMatchers(HttpMethod.GET, "/configuration/security").permitAll()
                .antMatchers(HttpMethod.GET, "/configuration/ui").permitAll()
                .anyRequest().authenticated();
//        .anyRequest().permitAll();
        http.exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        // @formatter:off
        resources.resourceId(resourceId);
        resources.tokenServices(tokenServices());
        // @formatter:on
    }

    /**
     * token存储,这里使用jwt方式存储
     * @return TokenStore
     */
    @Bean
    public TokenStore resourceJwtTokenStore() {
        return new JwtTokenStore(resourceAccessTokenConverter());
    }

    /**
     * Token转换器必须与认证服务一致
     * @return JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter resourceAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // RSA加密，加载公钥
        Resource resource = new ClassPathResource("public.txt");
        String publicKey;
        try {
            publicKey = IOUtils.toString(resource.getInputStream(), "UTF-8");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        converter.setVerifierKey(publicKey);
        // 对称加密
//        converter.setSigningKey(SecurityConstants.JWT_SIGNING_KEY);
        return converter;
    }

    /**
     * 创建一个默认的资源服务token
     * @return ResourceServerTokenServices
     */
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(resourceJwtTokenStore());
        return defaultTokenServices;
    }
}
