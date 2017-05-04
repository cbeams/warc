package org.iokit.imf;

import org.iokit.core.parse.InvalidCharacterException;
import org.iokit.core.parse.ParsingException;

import io.beams.valjo.ValjoSpec;

import org.junit.Test;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TokenSpec extends ValjoSpec {

    public TokenSpec() {
        super(
            "valid-token-A",
            "valid-token-B",
            "c",
            "12",
            "+!-"
        );
    }

    TokenSpec(String inputA, String inputB, String... moreInputs) {
        super(inputA, inputB, moreInputs);
    }

    @Override
    protected Object parse(String input) throws ParsingException {
        return Token.parse(input);
    }

    @Test
    public void parseInputWithControlCharacters() {
        assertThatThrownBy(() -> Token.parse(format("invalid%cinput", (char) 127)))
            .isInstanceOf(InvalidCharacterException.class);
    }

    @Test
    public void parseInputWithWhitespace() {
        assertThatThrownBy(() -> Token.parse("invalid input"))
            .isInstanceOf(InvalidCharacterException.class);
    }

    @Test
    public void parseInputWithSeparatorCharacter() {
        assertThatThrownBy(() -> Token.parse("invalid;input"))
            .isInstanceOf(InvalidCharacterException.class);
    }

    @Test
    public void parseInputWithSeparatorSequence() {
        // note that separator sequences are not checked for explicitly during token parsing.
        // All separator sequences contain a semicolon (';') character, meaning they will get
        // caught during normal separator character checks in any case.
        assertThatThrownBy(() -> Token.parse("invalid&lt;input"))
            .isInstanceOf(InvalidCharacterException.class);
    }

    @Test
    public void parseInputWithLinefeed() {
        assertThatThrownBy(() -> Token.parse("invalid\ninput"))
            .isInstanceOf(InvalidCharacterException.class);
    }

    @Test
    public void parseInputWithCRLF() {
        assertThatThrownBy(() -> Token.parse("invalid\r\ninput"))
            .isInstanceOf(InvalidCharacterException.class);
    }
}
