package com.iruch.practice.javacore.practice4;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.*;

class BlockingQueueTest {

    private ExecutorService executor;

    @BeforeEach
    void setUp() {
        executor = Executors.newFixedThreadPool(2);
    }

    @AfterEach
    void tearDown() {
        executor.shutdownNow();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("Очередь не создастся при отрицательном и нулевом значении ёмкости")
    void givenNonPositiveCapacity_whenCreateQueue_thenThrowsIllegalArgumentException(int capacity) {
        assertThatThrownBy(() -> new BlockingQueue<>(capacity)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("dequeue на пустой очереди ждёт элемент")
    void givenEmptyQueue_whenDequeue_thenWaitsUntilElementIsEnqueued() throws Exception {
        BlockingQueue<String> queue = new BlockingQueue<>(1);
        Future<String> future = executor.submit(queue::dequeue);
        Thread.sleep(200);
        assertThat(future).isNotDone();
        queue.enqueue("Маша");
        String value = future.get(1, TimeUnit.SECONDS);
        assertThat(value).isEqualTo("Маша");
    }

    @Test
    @DisplayName("enqueue на полной очереди ждёт места")
    void givenFullQueue_whenEnqueue_thenWaitsUntilElementIsDequeued() throws Exception {
        BlockingQueue<String> queue = new BlockingQueue<>(1);
        queue.enqueue("Петя");

        Future<String> future = executor.submit(() -> {
            queue.enqueue("Вася");
            return null;
        });
        Thread.sleep(200);

        assertThat(future).isNotDone();
        assertThat(queue.size()).isEqualTo(1);
        assertThat(queue.dequeue()).isEqualTo("Петя");

        future.get(1, TimeUnit.SECONDS);
        assertThat(queue.dequeue()).isEqualTo("Вася");
    }
}