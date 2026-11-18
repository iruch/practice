package com.iruch.practice.javacore.practice1;

public final class StringBuilderCast {

    private final StringBuilder builder =  new StringBuilder();

    // Один метод добавления строки
    public StringBuilderCast append(String str) {
        builder.append(str);
        return this;
    }
    // Сохраняем состояние
    public Snapshot save() {
        return new Snapshot(builder.toString());
    }

    // Возвращаем из вне при необходимости
    public void undo(Snapshot snapshot) {
        // Очищаем путём установки длины в 0
        builder.setLength(0);
        // Достаем сохраненное значение из снапшота
        builder.append(snapshot.text);
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    public static class Snapshot {
        private final String text;
        private Snapshot(String text) {
            this.text = text;
        }
    }
}
