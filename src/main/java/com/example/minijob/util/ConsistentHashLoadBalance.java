package com.example.minijob.util;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 不带虚拟节点的一致性Hash算法
 */
public class ConsistentHashLoadBalance extends AbstractLoadBalance {
    /**
     * key表示服务器的hash值，value表示服务器的名称
     */
    private static SortedMap<Integer, String> sortedMap =
            new TreeMap<Integer, String>();

    /**
     * 程序初始化，将所有的服务器放入sortedMap中
     */
    static {
        for (Map.Entry m : ChannelMap.serverWeightMap.entrySet()) {
            int hash = getHash(m.getKey().toString());
            System.out.println("[" + m.getKey().toString() + "]加入集合中, 其Hash值为" + hash);
            sortedMap.put(hash, m.getKey().toString());
        }
    }

    /**
     * 使用FNV1_32_HASH算法计算服务器的Hash值,这里不使用重写hashCode的方法，最终效果没区别
     */
    private static int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }

    /**
     * 得到应当路由到的结点
     */

    @Override
    public String getExecuteChannel(String node) {
        // 得到带路由的结点的Hash值
        int hash = getHash(node);
        // 得到大于该Hash值的所有Map
        SortedMap<Integer, String> subMap =
                sortedMap.tailMap(hash);
        // 没有大于该Hash值的map，直接获取第一个节点
        if (subMap.size() == 0) {
            return sortedMap.get(sortedMap.firstKey());
        }
        // 第一个Key就是顺时针过去离node最近的那个结点
        Integer i = subMap.firstKey();
        // 返回对应的服务器名称
        return subMap.get(i);
    }

    @Override
    public String getExecuteChannel() {
        return null;
    }
}