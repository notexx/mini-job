package com.example.minijob.util;

public enum LoadBalanceType {
    RANDOM_LOAD_BALANCE,
    CONSISTENT_HASH_LOAD_BALANCE,
    LOAD_BALANCE;

    LoadBalanceType() {
    }
}
