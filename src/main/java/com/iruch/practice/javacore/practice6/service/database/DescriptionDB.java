package com.iruch.practice.javacore.practice6.service.database;

import java.util.HashMap;
import java.util.Map;

public final class DescriptionDB implements ProductDatabase<String> {

    private static DescriptionDB INSTANCE;
    private final Map<String, String> data;

    private DescriptionDB() {
        data = new HashMap<>();
        data.put("Ноутбук", "Самый топовый на рынке ноутбук");
        data.put("Сапоги", "Сапоги ковбойские");
        data.put("Телевизор", "Телевизор SAMSUNG 75");
        data.put("iPhone 17 Pro MAX", "Новый айфоня ничем не отличающийся от старого");
        data.put("Дверь входная", "Металлическая, бронебойная дверь");
        data.put("Коврик для мыши", "Латексный и блестящий");
        data.put("GTA VI Standard Edition", "Убивалка времени ближайшие 10 лет");
        data.put("Калькулятор", "Простой калькулятор");
    }

    public static DescriptionDB getInstance() {
        synchronized (DescriptionDB.class) {
            if (INSTANCE == null) {
                INSTANCE = new DescriptionDB();
            }
        }
        return INSTANCE;
    }

    @Override
    public String getValue(String productName) {
        return data.get(productName);
    }

}
