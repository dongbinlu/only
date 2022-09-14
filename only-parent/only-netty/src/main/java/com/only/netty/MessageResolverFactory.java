package com.only.netty;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public final class MessageResolverFactory {

    /**
     * 收集系统中所有{@link Resolver} 接口的实现。 key为在spring容器中Bean的名字
     */
    @Autowired
    private Map<String, Resolver> resolverMap;

    private static final List<Resolver> resolvers = new CopyOnWriteArrayList<>();

    @PostConstruct
    public void init() {
        Set<Map.Entry<String, Resolver>> entrySet = resolverMap.entrySet();
        for (Map.Entry<String, Resolver> entry : entrySet) {
            registerResolver(entry.getValue());
        }
    }

    public void registerResolver(Resolver resolver) {
        resolvers.add(resolver);
    }

    /**
     * 根据解码后的消息，在工厂类处理器中查找可以处理当前消息的处理器
     *
     * @param message
     * @return
     */
    public Resolver getMessageResolver(Message message) {
        Resolver resolver_ = null;
        for (Resolver resolver : resolvers) {
            if (resolver.support(message)) {
                resolver_ = resolver;
                break;
            }
        }
        log.warn("Resolver type not found,flag:{},type:{},tag:{}", message.getFlag(), message.getType(), message.getTag());
        return resolver_;
    }

}