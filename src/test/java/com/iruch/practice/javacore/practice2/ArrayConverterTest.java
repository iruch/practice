package com.iruch.practice.javacore.practice2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

class ArrayConverterTest {

    @Test
    void ArrayCovert() {
        ArrayList<String> list = new ArrayList<>();
        String pattern = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int subLength = 10;
        int totalIterations = 200;
        int maxStartIndex = pattern.length() - subLength;
        for (int i = 0; i < totalIterations; i++) {
            int startIndex = ThreadLocalRandom.current().nextInt(maxStartIndex + 1);
            list.add(pattern.substring(startIndex, startIndex + subLength));
        }
        Map<String, Integer> map = ArrayConverter.convert(list);
        map.forEach((key, value) -> System.out.println(key + " -> " + value));
    }

}