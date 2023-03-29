package com.safecode.security.auth.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
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

    //数据源，读取第三方配置信息
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
        //直接配置数据源即可
        clients.jdbc(dataSource);

        //数据持久化，从数据库中读取
/*		
		clients.inMemory()
			.withClient("order-api") //应用的用户名
			.secret(passwordEncoder.encode("123456")) //应用的密码
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
        //AuthenticationManager，从哪来，先别管，先注入进来
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                //这个UserDetailsService是专门给refresh_token用的
                .userDetailsService(userDetailsService);
    }

    /*
     * 其他服务找认证服务验令牌，需要此方法配置
     * 谁能找我验这个令牌或者说验这个令牌需要什么样的权限
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        /*
         * checkTokenAccess()配用来检查token的这个服务的一个访问规则，也就是验checkToken的请求一定是要经过身份认证的
         * 也就是一定要带clientId和clientSecret，不带这两个参数不做校验处理
         */
        security.checkTokenAccess("isAuthenticated()");
    }

    /*
     * token存储,需要告诉认证服务器需要自己配置的tokenStore来存储Token
     */
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }
}
