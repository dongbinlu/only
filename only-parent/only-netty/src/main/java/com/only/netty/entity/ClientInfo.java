package com.only.netty.entity;


import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 点对点通讯
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientInfo {

    private String clientId;

    private String addr;

    private Channel channel;


}
