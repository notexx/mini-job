package com.example.minijob.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelMap {
    /**
     * 多组不同的执行通道，每组执行通道有1~M台服务器，作为任务执行终端
     */
    // 执行通道列表，Key代表执行通道，Value代表该Ip的权重
    public static Map<String, Integer> serverWeightMap = new ConcurrentHashMap<>();

    static {
        serverWeightMap.put("execute-channel-1", 1);
        serverWeightMap.put("execute-channel-2", 1);
        serverWeightMap.put("execute-channel-3", 1);
        serverWeightMap.put("execute-channel-4", 1);
    }
}