package com.example.minijob.util;

import java.util.*;

/**
 * 随机加权轮询算法
 * RandomLoadBalance 的算法思想比较简单，在经过多次请求后，能够将调用请求按照权重值进行“均匀”分配。
 * 当然 RandomLoadBalance 也存在一定的缺点，当调用次数比较少时，Random 产生的随机数可能会比较集中，此时多数请求会落到同一台服务器上
 */
public class RandomLoadBalance extends AbstractLoadBalance{
    private final Random random = new Random();

    @Override
    public String getExecuteChannel(String node) {
        return null;
    }

    @Override
    public String getExecuteChannel() {
        Map<String, Integer> serverWeightMap = ChannelMap.serverWeightMap;
        List<String> keyList = new ArrayList<>();
        for (Map.Entry m : serverWeightMap.entrySet()) {
            for (int i = 0; i < Integer.valueOf(m.getValue().toString()); i++) {
                keyList.add(m.getKey().toString());
            }
        }
        String channel = keyList.get(random.nextInt(keyList.size()));
        return channel;
    }

}