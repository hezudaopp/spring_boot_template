package com.youmayon.tutorial.config;

import com.youmayon.tutorial.constant.SecurityConstants;
import com.youmayon.tutorial.domain.User;
import com.youmayon.tutorial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jawinton on 26/06/2017.
 * Oauth2 配置
 */
@Configuration
@EnableAuthorizationServer // 必须
@EnableResourceServer //必须
public class Oauth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Value("${security.oauth2.resource.id:spring-boot-template}") // 默认值spring-boot-application
    private String resourceId;

    @Autowired
    private UserService userService; // 引入security中提供的 UserDetailsService

    @Autowired
    private AuthenticationManager authenticationManager; // 引入security中提供的 AuthenticationManager

    private class CustomTokenEnhancer implements TokenEnhancer {
        @Override
        public OAuth2AccessToken enhance(
                OAuth2AccessToken accessToken,
                OAuth2Authentication authentication) {
            Map<String, Object> additionalInfo = new HashMap<>();
            User user = (User) authentication.getUserAuthentication().getPrincipal();
            additionalInfo.put("userId", user.getId());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        }
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    /**
     * token存储,这里使用jwt方式存储
     * @return TokenStore
     */
    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * Token转换器
     * @return JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter(){
//            /***
//             * 重写增强token方法,用于自定义一些token返回的信息
//             */
//            @Override
//            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//                String userName = authentication.getUserAuthentication().getName();
//                User user = (User) authentication.getUserAuthentication().getPrincipal();// 与登录时候放进去的UserDetail实现类一直查看link{SecurityConfiguration}
//                /** 自定义一些token属性 ***/
//                final Map<String, Object> additionalInformation = new HashMap<>();
//                additionalInformation.put("id", user.getId());
//                additionalInformation.put("userName", userName);
//                additionalInformation.put("roles", user.getAuthorities());
//                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
//                OAuth2AccessToken enhancedToken = super.enhance(accessToken, authentication);
//                return enhancedToken;
//            }
//        };
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SecurityConstants.JWT_SIGNING_KEY);
        return converter;
    }

//    @Value("${spring.datasource.driver-class-name}")
//    private String oauthClass;
//
//    @Value("${spring.datasource.url}")
//    private String oauthUrl;
//
//    @Value("${spring.datasource.username}")
//    private String username;
//
//    @Value("${spring.datasource.password}")
//    private String password;

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
//    @Bean
//    public TokenStore mysqlTokenStore() {
//        DataSource tokenDataSource = DataSourceBuilder.create()
//                .driverClassName(oauthClass)
//                .username(username)
//                .password(password)
//                .url(oauthUrl)
//                .build();
//        return new JdbcTokenStore(tokenDataSource);
//    }
//
//     @Bean
//    public TokenStore InMemoryTokenStore() {
//        return new InMemoryTokenStore(); //使用内存中的 token store
//    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
//        oauthServer
//                .tokenKeyAccess("permitAll()")
//                .checkTokenAccess("isAuthenticated()");
        oauthServer.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_CLIENT')");
        oauthServer.checkTokenAccess("hasAuthority('ROLE_CLIENT')");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("android") // 配置默认的client
                .secret("android_secret_key")
                .authorities("ROLE_CLIENT").authorizedGrantTypes("password", "refresh_token")
                .scopes("read", "write").autoApprove("read")
                .resourceIds(resourceId)
                .accessTokenValiditySeconds(7200)
                .refreshTokenValiditySeconds(86400 * 30)
                .and().withClient("ios").authorizedGrantTypes("authorization_code", "implicit")
                .authorities("ROLE_CLIENT").scopes("read", "write").resourceIds(resourceId)
                .accessTokenValiditySeconds(7200);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
        endpoints
                .tokenStore(jwtTokenStore())
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager)
                .userDetailsService(userService);
    }
}