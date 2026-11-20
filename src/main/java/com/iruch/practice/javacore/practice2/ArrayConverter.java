package com.iruch.practice.javacore.practice2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class ArrayConverter {

    public static <T> Map<T, Integer> convert(ArrayList<T> source) {
        Map<T, Integer> result = new HashMap<>();
        source.forEach(item -> result.merge(item, 1, Integer::sum));
        return result;
    }

}
