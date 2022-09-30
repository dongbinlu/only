package com.only.base.constant;

/**
 * 常量类
 * 接口中的每个成员变量都默认使用public static final修饰。
 */

public interface OnlyConstants {

    /**
     * 接口不能用final修饰 ， 接口如果用final修饰无法被子类实现，不符合多态
     */
    interface Netty {
        /**
         * TCP 端口
         */
        int TCP_PORT = 18090;
        /**
         * bossGroup默认实例化1个线程
         */
        int BOSS_GROUP_THREAD = 1;
        /**
         * 读空闲超时时间
         */
        int READER_IDLE_TIME = 10;
        /**
         * 写空闲超时时间
         */
        int WRITER_IDLE_TIME = 0;
        /**
         * 读写空闲超时时间
         */
        int ALL_IDLE_TIME = 0;
        /**
         * WebSocket握手的协议前缀
         */
        String WEB_SOCKET_PREFIX = "GET /";
        /**
         * 默认暗号长度为23
         */
        int MAX_LENGTH = 23;
        /**
         * /ws为访问websocket时的uri
         */
        String WEB_SOCKET_PATH = "/ws";
        /**
         * 读空闲超时次数
         */
        int READ_IDLE_TIMES = 3;
        /**
         * 可读字节数（总消息可读字节数）
         */
        int READABLE_BYTES_SIZE = 7;
        /**
         * 可读字节数（消息可读字节数）
         */
        int READABLE_BYTES_SIZE_MESSAGE = 4;

        /**
         * 协议相关标识
         */
        class Protocol {
            /**
             * TYPE[0] 请求标识
             * TYPE[1] 响应标识
             */
            public static final byte[] TYPE = {0x00, 0x01};
        }

        class WebSocketProtocol extends Protocol {
            public static final byte FLAG = 0x00;
            /**
             * 0x00：history
             */
            public static final byte[] TAG = {0x00};
            public static final String VALUE = "0x00";
        }

        class OnlyProtocol extends Protocol {
            public static final byte FLAG = (byte) 0xD1;
            public static final byte[] TAG = {0x00};
        }

        class TcpProtocol extends Protocol {
            public static final byte FLAG = (byte) 0xD0;
            /**
             * 0x01：register , 0x02: status
             */
            public static final byte[] TAG = {0x01, 0x02};
        }

    }

    interface Auth {

    }

    interface MultiDS {

        /**
         * 多库 多表策略
         */
        String ROUTING_DS_TABLE_STATEGY = "ROUTING_DS_TABLE_STATEGY";

        /**
         * 多库 一表策略
         */
        String ROUTGING_DS_STATEGY = "ROUTGING_DS_STATEGY";

        /**
         * 一库多表策略
         */
        String ROUTGIN_TABLE_STATEGY = "ROUTGIN_TABLE_STATEGY";

        String DEFAULT_ROUTING_FIELD = "userId";

    }

}
