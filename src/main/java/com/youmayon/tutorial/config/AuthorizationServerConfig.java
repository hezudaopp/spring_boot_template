package com.youmayon.tutorial.config;

import com.youmayon.tutorial.constant.SecurityConstants;
import com.youmayon.tutorial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * Created by Jawinton on 26/06/2017.
 * Oauth2 配置
 */
@Configuration
@EnableAuthorizationServer // 必须
@EnableResourceServer //必须
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private UserService userService; // 引入security中提供的 UserDetailsService

    @Autowired
    private AuthenticationManager authenticationManager; // 引入security中提供的 AuthenticationManager

    @Value("${spring.datasource.driver-class-name}")
    private String oauthClass;

    @Value("${spring.datasource.url}")
    private String oauthUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    /**
     * 使用数据库持久化token
     * 需要先创建oauth_access_token表和oauth_refresh_token表
     create table oauth_access_token (
     token_id VARCHAR(256),
     token BLOB,
     authentication_id VARCHAR(256) PRIMARY KEY,
     user_name VARCHAR(256),
     client_id VARCHAR(256),
     authentication BLOB,
     refresh_token VARCHAR(256)
     );

     create table oauth_refresh_token (
     token_id VARCHAR(256),
     token BLOB,
     authentication BLOB
     );
     */
    @Bean
    public TokenStore tokenStore() {
        DataSource tokenDataSource = DataSourceBuilder.create()
                .driverClassName(oauthClass)
                .username(username)
                .password(password)
                .url(oauthUrl)
                .build();
        return new JdbcTokenStore(tokenDataSource);
    }

    //    @Bean
//    public TokenStore tokenStore() {
//        return new InMemoryTokenStore(); //使用内存中的 token store
//    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(SecurityConstants.CLIENT_ID) // 配置默认的client
                .secret(SecurityConstants.CLIENT_SECRET)
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read").autoApprove("read");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .userDetailsService(userService);
    }
}