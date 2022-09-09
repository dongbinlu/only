package com.only.netty;

import lombok.*;

/**
 * 自定义协议包
 */
@Builder
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    /**
     * 标识 1字节
     * 0x00代表webSocket
     * 0xD0代表平台标识
     */
    private byte flag;

    /**
     * 类型 1字节
     * 0：请求类型
     * 1：响应类型
     */
    private byte type;

    /**
     * 业务标识 1字节
     * 子域tag标签
     */
    private byte tag;

    /**
     * 定义一次发送包体长度（value长度）  4字节
     */
    private int length;

    /**
     * 一次发送包体内容
     */
    private byte[] value;


}
