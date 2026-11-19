package com.iruch.practice.javacore.practice2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ArrayConverter: конвертация массива в Map")
class ArrayConverterTest {

    @Test
    @DisplayName("Пустой список даёт пустой словарь")
    void emptyList_ReturnsEmptyMap() {
        ArrayList<String> list = new ArrayList<>();
        Map<String, Integer> result = ArrayConverter.convert(list);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Одинаковые строки считаются вместе")
    void repeatedStrings_areCountedTogether() {
        ArrayList<String> list = new ArrayList<>(List.of("a", "b", "a", "a"));
        Map<String, Integer> result = ArrayConverter.convert(list);
        assertEquals(Map.of("a", 3, "b", 1), result);
    }

    @Test
    @DisplayName("Сумма всех счётчиков равна размеру списка")
    void sumOfCounts_equalsListSize() {
        int subLength = 10;
        int totalIterations = 200;
        String pattern = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int maxStartIndex = pattern.length() - subLength;
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < totalIterations; i++) {
            int startIndex = ThreadLocalRandom.current().nextInt(maxStartIndex + 1);
            list.add(pattern.substring(startIndex, startIndex + subLength));
        }
        Map<String, Integer> result = ArrayConverter.convert(list);
        int sum = result.values().stream().mapToInt(Integer::intValue).sum();
        assertEquals(totalIterations, sum);
    }
}