package com.only.rpc.client.config;

import com.only.rpc.client.rpc.RpcClientFrame;
import com.only.rpc.remote.SendSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类说明：
 */
@Configuration
public class BeanConfig {

    @Autowired
    private RpcClientFrame rpcClientFrame;

    @Bean
    public SendSms getSmsService() throws Exception{
        return rpcClientFrame.getRemoteProxyObject(SendSms.class);
    }
}
