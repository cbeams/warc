package org.iokit.general;

import org.iokit.core.IOKitInputStream;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LineInputStreamSpec {

    @Test
    public void nullInput() {
        //noinspection ConstantConditions
        assertThatThrownBy(() -> new IOKitInputStream(null))
            .isInstanceOf(NullPointerException.class);
    }
}
