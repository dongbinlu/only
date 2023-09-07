package com.only.netty.adv.server.asyncpro;


import com.only.netty.adv.vo.MyMessage;

/**
 * @description ：消息转任务处理器
 */
public interface ITaskProcessor {

    Runnable execAsyncTask(MyMessage msg);

}
