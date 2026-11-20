package com.iruch.practice.javacore.practice3;

import lombok.Getter;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamAPI {

    enum DayOfWeek {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY
    }

    @Getter
    static class WorkSheet {
        String name;
        List<DayOfWeek> workDays;

        public WorkSheet(String name, List<DayOfWeek> workDays) {
            this.name = name;
            this.workDays = workDays;
        }
    }

    void test() {

        var denSheet = new WorkSheet("Денис", List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SUNDAY));
        var benSheet = new WorkSheet("Бен", List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY));
        var lisSheet = new WorkSheet("Лиза", List.of(DayOfWeek.THURSDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        List<WorkSheet> sheets = List.of(denSheet, benSheet, lisSheet);
        List<DayOfWeek> list = getResult(sheets);
        list.forEach(System.out::println);
    }

    static List<DayOfWeek> getResult(List<WorkSheet> sheets) {
        return sheets.stream()
                .flatMap(workSheet -> workSheet.getWorkDays().stream())
                .collect(Collectors.groupingBy(dayOfWeek -> dayOfWeek,
                        () -> new EnumMap<>(DayOfWeek.class),
                        Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList();
    }
}
