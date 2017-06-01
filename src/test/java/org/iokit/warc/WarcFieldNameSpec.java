package org.iokit.warc;

import org.iokit.general.InvalidCharacterException;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WarcFieldNameSpec {

    @Test
    public void parseInputWithSpecialCharacters() {
        for (char c : new WarcSpecials().chars())
            assertThatThrownBy(() -> new WarcFieldName.Parser().parse(String.format("Invalid-%c-Name", c)))
                .isInstanceOf(InvalidCharacterException.class);
    }
}
