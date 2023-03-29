package com.safecode.security.auth.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@EnableJdbcHttpSession
@Configuration
// 开启认证授权服务
@EnableAuthorizationServer
public class OAuth2AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    /*
     * PasswordEncoder和AuthenticationManager还没有地方来
     */

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    // 数据源，读取第三方配置信息
    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    /*
     * 配置客户端应用
     * ClientDetailsServiceConfigurer：客户端详情服务的配置
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 直接配置数据源即可
        clients.jdbc(dataSource);

        // 数据持久化，从数据库中读取
		/*		
				clients.inMemory()
					.withClient("order-api")
					.secret(passwordEncoder.encode("123456"))
					.scopes("read" , "write")
					.accessTokenValiditySeconds(3600)
					.resourceIds("order-server")//发给order-api的token能访问那些资源服务器
					.authorizedGrantTypes("password")
					.and()
					.withClient("order-service")
					.secret(passwordEncoder.encode("123456"))
					.scopes("read" , "write")
					.accessTokenValiditySeconds(3600)
					.resourceIds("order-server")
					.authorizedGrantTypes("password");
		*/
    }

    /*
     * 用户相关的配置，哪些用户来访问认证服务器
     * AuthenticationManager来确定传进来的用户信息是不是合法的
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // AuthenticationManager，从哪来，先别管，先注入进来
        endpoints.authenticationManager(authenticationManager)
                // 这个UserDetailsService是专门给refresh_token用的
                .userDetailsService(userDetailsService)
                .tokenStore(jwtTokenStore());

        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> enhancers = new ArrayList<>();
        enhancers.add(jwtTokenEnhancer());
        enhancers.add(jwtAccessTokenConverter());
        enhancerChain.setTokenEnhancers(enhancers);
        endpoints.tokenEnhancer(enhancerChain);
    }

    /*
     * 其他服务找认证服务验令牌，需要此方法配置
     * 谁能找我验这个令牌或者说验这个令牌需要什么样的权限
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        /*
         * checkTokenAccess()配用来检查token的这个服务的一个访问规则，也就是验checkToken的请求一定是要经过身份认证的
         */
        security.checkTokenAccess("isAuthenticated()")
                /*
                 * tokenKeyAccess("isAuthenticated()"),接受token的人需要key来验这个签名，所以需要把这个key通过服务暴露出去，
                 * 这样使用token的人才能通过这个服务拿到这个key,来验证这个签名
                 */
                .tokenKeyAccess("isAuthenticated()");
    }

    /*
     * token存储,需要告诉认证服务器需要自己配置的tokenStore来存储Token
     */
    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /*
     * 必须是共有的，必须声明为spring的组件，才会将/oauth/tokey_key暴露出去
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        /*
         * 签名的键，JWT本身并不是加密的，谁都可以看到令牌里的信息，它用来保证安全性的方式就是这个签名，需要用这个key对token进行签名，然后
         * 使用token的人，使用相同的key去验这个签名，如果这个签名证明是这个key签出去的，那么就说明token的信息没有被改过，认为是安全的，但是
         * 里面的信息是透明的，谁都可以看到
         *
         * 接受token的人需要key来验这个签名，所以需要把这个key通过服务暴露出去，这样使用token的人才能通过这个服务拿到这个key,来验证这个签名
         */
        // 不用字符串做签名，用证书做签名
        // converter.setSigningKey("123456");
        // new KeyStoreKeyFactory(arg1,arg2); arg1:证书，arg2:密码
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("boy.key"),
                "123456".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("boy"));
        return converter;
    }

    /*
     * token增强器
     */
    @Bean
    @ConditionalOnBean(TokenEnhancer.class)
    public TokenEnhancer jwtTokenEnhancer() {
        return new JwtTokenEnhancer();
    }
}
