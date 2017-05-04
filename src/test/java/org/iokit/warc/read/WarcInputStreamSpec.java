package org.iokit.warc.read;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WarcInputStreamSpec {

    @Test
    public void nullInput() {
        //noinspection ConstantConditions
        assertThatThrownBy(() -> new WarcInputStream(null))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
