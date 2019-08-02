package com.example.minijob.util;

import java.util.*;

/**
 * 轮询算法
 * LoadBalance 算法 依次遍历通道
 * 缺点：每个执行通道性能不一样，可能导致性能差的执行缓慢，而性能好的，处于未饱和状态
 */
public class LoadBalance extends AbstractLoadBalance{
    private static Integer pos = 0;

    @Override
    public String getExecuteChannel(String node) {
        return null;
    }

    @Override
    public String getExecuteChannel() {

        // 取得Ip地址List
        Set<String> keySet = ChannelMap.serverWeightMap.keySet();
        List<String> keyList = new ArrayList<>();
        keyList.addAll(keySet);

        String channel = null;
        synchronized (pos) {
            if (pos >= keySet.size()){
                pos = 0;
            }
            channel = keyList.get(pos);
            pos++;
        }

        return channel;
    }

}