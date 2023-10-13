package com.only.netty.client.sync;

import java.util.Map;
import java.util.concurrent.*;

public class SyncDemo {

    private static Map<String, CompletableFuture<String>> requestMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {

        new Thread(()->{
            String result = null;
            try {
                result = send();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println(result);
        }).start();


        Thread.sleep(100000);
        new Thread(()->{
            receive();
        }).start();


    }


    /**
     * 请求——响应
     *
     * @return
     */
    public static String send() throws TimeoutException, ExecutionException {
        System.out.println("send ...");
        String requestId = "1001";

        /**
         * 处理异步编程和并发操作
         */
        CompletableFuture<String> future = new CompletableFuture();
        requestMap.put(requestId, future);

        // TODO 发送远程调用
        // 。。。。。。

        String result = null;
        try {
            result = future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();// 恢复中断状态
        } catch (ExecutionException e) {
            throw new ExecutionException(e.getCause());
        } catch (TimeoutException e) {
            throw new TimeoutException("超时异常");
        } finally {
            requestMap.remove(requestId);
        }

        return result;
    }


    /**
     * 响应——类似netty的channelRead
     */
    public static void receive() {
        System.out.println("receive ...");
        String requestId = "1001";

        CompletableFuture<String> future = requestMap.get(requestId);
        if (future != null) {
            future.complete("hello");
            requestMap.remove(requestId);
        }

    }


}
