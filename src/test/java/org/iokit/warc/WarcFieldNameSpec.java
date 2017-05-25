package org.iokit.warc;

import org.iokit.message.TokenSpec;

import org.iokit.core.validate.InvalidCharacterException;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WarcFieldNameSpec extends TokenSpec {

    public WarcFieldNameSpec() {
        super(
            "Valid-Name-A",
            "Valid-Name-B"
        );
    }

    @Override
    protected Object parse(String input) {
        return new WarcFieldName.Parser().parse(input);
    }

    @Test
    public void parseInputWithSpecialCharacters() {
        for (char c : new WarcSpecials().chars())
            assertThatThrownBy(() -> parse(String.format("Invalid-%c-Name", c)))
                .isInstanceOf(InvalidCharacterException.class);
    }
}
