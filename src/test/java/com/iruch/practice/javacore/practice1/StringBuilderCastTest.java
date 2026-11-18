package com.iruch.practice.javacore.practice1;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class StringBuilderCastTest {

    @Test
    void undoRestoresStateFromSnapshot() {
        StringBuilderCast sbc = new StringBuilderCast();
        sbc.append("First");
        sbc.append("Second");
        StringBuilderCast.Snapshot state = sbc.save();
        sbc.append("Third");
        sbc.undo(state);
        assertThat(sbc.toString()).isEqualTo("FirstSecond");
    }

}