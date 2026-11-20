package com.iruch.practice.javacore.practice3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StreamAPI: группировка в стриме по полю")
class StreamAPITest {


    @Test
    @DisplayName("Дни выводятся когда в них работало более 1 человека")
    void givenDayWithTwoWorkers_whenFindDaysWithSeveralWorkers_thenReturnsOnlyDayWithTwoWorkers() {
        var denSheet = new StreamAPI.WorkSheet("Денис", List.of(StreamAPI.DayOfWeek.TUESDAY, StreamAPI.DayOfWeek.THURSDAY, StreamAPI.DayOfWeek.SUNDAY));
        var benSheet = new StreamAPI.WorkSheet("Бен", List.of(StreamAPI.DayOfWeek.MONDAY, StreamAPI.DayOfWeek.WEDNESDAY, StreamAPI.DayOfWeek.SATURDAY));
        var lisSheet = new StreamAPI.WorkSheet("Лиза", List.of(StreamAPI.DayOfWeek.THURSDAY, StreamAPI.DayOfWeek.WEDNESDAY, StreamAPI.DayOfWeek.FRIDAY));
        List<StreamAPI.WorkSheet> sheets = List.of(denSheet, benSheet, lisSheet);
        List<StreamAPI.DayOfWeek> list = StreamAPI.getResult(sheets);
        assertThat(list).containsExactly(StreamAPI.DayOfWeek.WEDNESDAY, StreamAPI.DayOfWeek.THURSDAY);
    }

}