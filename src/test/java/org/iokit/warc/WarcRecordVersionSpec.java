package org.iokit.warc;

import org.iokit.core.validate.ValidatorException;

import io.beams.valjo.ValjoSpec;

import org.junit.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WarcRecordVersionSpec extends ValjoSpec {

    public WarcRecordVersionSpec() {
        super(
            "WARC/1.0",
            "WARC/1.1"
        );
    }

    @Override
    protected Object parse(String input) {
        return new WarcVersion.Parser().parse(input);
    }

    @Override
    @Test
    public void parseEmptyInput() {
        assertThatThrownBy(() -> parse(""))
            .isInstanceOf(ValidatorException.class);
    }

    @Override
    @Test
    public void parseBlankInput() {
        Stream.of(" ", "\t")
            .forEach(blank ->
                assertThatThrownBy(() -> parse(blank))
                    .isInstanceOf(ValidatorException.class));
    }
}
