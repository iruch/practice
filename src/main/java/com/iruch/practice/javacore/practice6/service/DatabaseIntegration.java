package com.iruch.practice.javacore.practice6.service;

public interface DatabaseIntegration<V> {
    V fetchValue(String productName) throws InterruptedException;
}
