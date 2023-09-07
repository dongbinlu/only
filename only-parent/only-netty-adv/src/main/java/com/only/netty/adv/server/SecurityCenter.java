package com.only.netty.adv.server;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @description ：TODO
 */
public class SecurityCenter {

    //用以检查用户是否重复登录的缓存
    private static Map<String, Boolean> nodeCheck = new ConcurrentHashMap<String, Boolean>();
    //用户登录的白名单
    private static Set<String> whiteList = new CopyOnWriteArraySet<>();
    static{
        whiteList.add("127.0.0.1");
        whiteList.add("10.0.80.30");
    }

    public static boolean isWhiteIP(String ip){
        return whiteList.contains(ip);
    }

    public static boolean isDupLog(String usrInfo){
        return nodeCheck.containsKey(usrInfo);
    }

    public static void addLoginUser(String usrInfo){
        nodeCheck.put(usrInfo,true);
    }

    public static void removeLoginUser(String usrInfo){
        nodeCheck.remove(usrInfo,true);
    }

}
