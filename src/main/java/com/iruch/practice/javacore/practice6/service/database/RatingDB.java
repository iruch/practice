package com.iruch.practice.javacore.practice6.service.database;

import java.util.HashMap;
import java.util.Map;

public final class RatingDB implements ProductDatabase<Double> {

    private static RatingDB INSTANCE;
    private final Map<String, Double> data;

    private RatingDB() {
        data = new HashMap<>();
        data.put("Ноутбук", 2.2);
        data.put("Сапоги", 3.5);
        data.put("Телевизор", 4.8);
        data.put("iPhone 17 Pro MAX", 4.9);
        data.put("Дверь входная", 4.1);
        data.put("Коврик для мыши", 2.2);
        data.put("GTA VI Standard Edition", 5.0);
        data.put("Калькулятор", 4.5);
    }

    public static RatingDB getInstance() {
        synchronized (RatingDB.class) {
            if (INSTANCE == null) {
                INSTANCE = new RatingDB();
            }
        }
        return INSTANCE;
    }

    @Override
    public Double getValue(String productName) {
        return data.get(productName);
    }

}
