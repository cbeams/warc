package org.iokit.core.input;

import org.iokit.line.LineInputStream;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LineInputStreamSpec {

    @Test
    public void nullInput() {
        //noinspection ConstantConditions
        assertThatThrownBy(() -> new LineInputStream(null))
            .isInstanceOf(NullPointerException.class);
    }
}
