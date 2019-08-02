package com.example.minijob.util;

public abstract class AbstractLoadBalance {
    public abstract String getExecuteChannel(String node);
    public abstract String getExecuteChannel();
}
