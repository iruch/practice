package com.iruch.practice.javacore.practice4;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueue<T> {

    private final Queue<T> elements;
    private final int capacity;

    public BlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Очередь не может быть <= 0");
        }
        this.elements = new LinkedList<>();
        this.capacity = capacity;
    }

    public synchronized void enqueue(T value) throws InterruptedException {
        while (elements.size() == capacity) {
            wait();
        }
        elements.add(value);
        notifyAll();
    }

    public synchronized T dequeue() throws InterruptedException {
        while (elements.isEmpty()) {
            wait();
        }
        T value = elements.poll();
        notifyAll();
        return value;
    }

    public synchronized int size() {
        return elements.size();
    }
}
