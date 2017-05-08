package org.iokit.core.parse;

import org.iokit.core.validate.InvalidCharacterException;

import io.beams.valjo.ValjoSpec;

import org.junit.Test;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TokenSpec extends ValjoSpec {

    private final SimpleTokenParser tokenParser = new SimpleTokenParser();

    public TokenSpec() {
        super(
            "valid-token-A",
            "valid-token-B",
            "c",
            "12",
            "+!-"
        );
    }

    protected TokenSpec(String inputA, String inputB, String... moreInputs) {
        super(inputA, inputB, moreInputs);
    }

    @Override
    protected Object parse(String input) throws ParsingException {
        return tokenParser.parse(input);
    }

    @Test
    public void parseInputWithControlCharacters() {
        assertThatThrownBy(() -> tokenParser.parse(format("invalid%cinput", (char) 127)))
            .isInstanceOf(ParsingException.class)
            .hasCauseInstanceOf(InvalidCharacterException.class);
    }

    @Test
    public void parseInputWithWhitespace() {
        assertThatThrownBy(() -> tokenParser.parse("invalid input"))
            .isInstanceOf(ParsingException.class)
            .hasCauseInstanceOf(InvalidCharacterException.class);
    }

    @Test
    public void parseInputWithSeparatorCharacter() {
        assertThatThrownBy(() -> tokenParser.parse("invalid;input"))
            .isInstanceOf(ParsingException.class)
            .hasCauseInstanceOf(InvalidCharacterException.class);
    }

    @Test
    public void parseInputWithSeparatorSequence() {
        // note that separator sequences are not checked for explicitly during token parsing.
        // All separator sequences contain a semicolon (';') character, meaning they will get
        // caught during normal separator character checks in any case.
        assertThatThrownBy(() -> tokenParser.parse("invalid&lt;input"))
            .isInstanceOf(ParsingException.class)
            .hasCauseInstanceOf(InvalidCharacterException.class);
    }

    @Test
    public void parseInputWithLinefeed() {
        assertThatThrownBy(() -> tokenParser.parse("invalid\ninput"))
            .isInstanceOf(ParsingException.class)
            .hasCauseInstanceOf(InvalidCharacterException.class);
    }

    @Test
    public void parseInputWithCRLF() {
        assertThatThrownBy(() -> tokenParser.parse("invalid\r\ninput"))
            .isInstanceOf(ParsingException.class)
            .hasCauseInstanceOf(InvalidCharacterException.class);
    }
}
