package com.iruch.practice.javacore.practice6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CompletableFuture: обработка ответов из трех сервисов")
class DataAggregatorTest {

    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "Ноутбук, Самый топовый на рынке ноутбук, 2399.0, 2.2",
            "Сапоги, Сапоги ковбойские, 2200.0, 3.5",
            "Телевизор, Телевизор SAMSUNG 75, 120000.0, 4.8",
            "iPhone 17 Pro MAX, Новый айфоня ничем не отличающийся от старого, 160000.0, 4.9",
            "GTA VI Standard Edition, Убивалка времени ближайшие 10 лет, 8999.0, 5.0"
    })
    @DisplayName("Данные из сервисов получены корректно")
    @Timeout(value = 5)
    void givenKnownProduct_whenAggregate_thenEachFieldIsRealOrFallback(String name, String description, double price, double rating) {
        try (DataAggregator dataAggregator = new DataAggregator()) {
            ProductInfo product = dataAggregator.aggregateProductInfo(name);
            assertThat(product.getName()).isEqualTo(name);
            assertThat(product.getDescription()).isIn(description, "Нет данных");
            assertThat(product.getPrice()).isIn(price, 0.0);
            assertThat(product.getRating()).isIn(rating, 0.0);
        }
    }
}