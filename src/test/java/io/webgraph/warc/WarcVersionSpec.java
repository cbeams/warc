package io.webgraph.warc;

import org.iokit.core.IOKitException;

import org.junit.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WarcVersionSpec {

    private Object parse(String input) {
        return new WarcVersion.Parser().parse(input);
    }

    @Test
    public void parseEmptyInput() {
        assertThatThrownBy(() -> parse(""))
            .isInstanceOf(IOKitException.class);
    }

    @Test
    public void parseBlankInput() {
        Stream.of(" ", "\t")
            .forEach(blank ->
                assertThatThrownBy(() -> parse(blank))
                    .isInstanceOf(IOKitException.class));
    }
}
