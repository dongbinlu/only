package com.only.test.boot.netty.sync.server;

import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/msg")
public class MessageController {

    @Autowired
    private NettyServerHandler nettyServerHandler;

    @GetMapping("/send/{key}/{msg}")
    public String send(@PathVariable("key") String key, @PathVariable("msg") String msg) throws ExecutionException, InterruptedException {

        Channel channel = nettyServerHandler.getChannelMap().get(key);
        CompletableFuture<String> future = nettyServerHandler.sendAndReceive(channel, msg);
        String result = future.get();
        return result;
    }

}