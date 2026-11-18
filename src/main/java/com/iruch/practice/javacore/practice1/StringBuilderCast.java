package com.iruch.practice.javacore.practice1;

public final class StringBuilderCast {

    private final StringBuilder builder =  new StringBuilder();

    public StringBuilderCast append(String str) {
        builder.append(str);
        return this;
    }

    public Snapshot save() {
        return new Snapshot(builder.toString());
    }

    public void undo(Snapshot snapshot) {
        builder.setLength(0);
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
