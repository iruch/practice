package com.iruch.practice.javacore.practice6.service;

import com.iruch.practice.javacore.practice6.service.database.ProductDatabase;

import java.util.concurrent.ThreadLocalRandom;

public class IntegrationService<V> implements DatabaseIntegration<V> {

    private final ProductDatabase<V> database;

    public IntegrationService(ProductDatabase<V> database) {
        if (database == null) {
            throw new RuntimeException("База данных недоступна");
        }
        this.database = database;
    }

    @Override
    public V fetchValue(String productName) throws InterruptedException {
        V value = database.getValue(productName);
        if (value == null) {
            throw new RuntimeException("Продукт не найден");
        }
        Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
        if (ThreadLocalRandom.current().nextDouble() < 0.2) {
            throw new RuntimeException("Сервис временно недоступен");
        }
        return value;
    }
}
