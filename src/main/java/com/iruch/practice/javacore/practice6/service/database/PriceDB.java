package com.iruch.practice.javacore.practice6.service.database;

import java.util.HashMap;
import java.util.Map;

public final class PriceDB implements ProductDatabase<Double> {

    private static PriceDB INSTANCE;
    private final Map<String, Double> data;

    private PriceDB() {
        data = new HashMap<>();
        data.put("Ноутбук", 2399.00);
        data.put("Сапоги", 2200.00);
        data.put("Телевизор", 120000.00);
        data.put("iPhone 17 Pro MAX", 160000.00);
        data.put("Дверь входная", 25000.00);
        data.put("Коврик для мыши", 1400.00);
        data.put("GTA VI Standard Edition", 8999.00);
        data.put("Калькулятор", 500.00);
    }

    public static PriceDB getInstance() {
        synchronized (PriceDB.class) {
            if (INSTANCE == null) {
                INSTANCE = new PriceDB();
            }
        }
        return INSTANCE;
    }

    @Override
    public Double getValue(String productName) {
        return data.get(productName);
    }

}
