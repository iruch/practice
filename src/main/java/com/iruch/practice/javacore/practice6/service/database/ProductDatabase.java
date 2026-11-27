package com.iruch.practice.javacore.practice6.service.database;

public interface ProductDatabase<V> {
    V getValue(String productName);
}
