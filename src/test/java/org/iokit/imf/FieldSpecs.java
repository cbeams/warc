package org.iokit.imf;

import org.iokit.core.parse.InvalidCharacterException;
import org.iokit.core.parse.ParsingException;

import io.beams.valjo.ValjoSpec;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.stream.IntStream;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.iokit.imf.Field.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    FieldSpecs.FieldSpec.class,
    FieldSpecs.NameSpec.class,
    FieldSpecs.ValueSpec.class
})
public class FieldSpecs {

    public static class FieldSpec extends ValjoSpec {

        public FieldSpec() throws ParsingException {
            super(
                "Valid-Name: valid value A",
                "Valid-Name: valid value B"
            );
        }

        @Override
        protected Object parse(String input) throws ParsingException {
            return Field.parse(input);
        }

        @Test
        public void parseSplitsNameAndValueOnSeparator() throws ParsingException {
            Field field = Field.parse("Valid-Name: valid value");
            assertThat(field.getName()).hasToString("Valid-Name");
            assertThat(field.getValue()).hasToString("valid value");
        }

        @Test
        public void parseInputWithoutFieldSeparator() {
            assertThatThrownBy(() -> Field.parse("invalid input"))
                .isInstanceOf(MissingSeparatorException.class);
        }
    }


    public static class NameSpec extends TokenSpec {

        public NameSpec() {
            super(
                "Valid-Name-A",
                "Valid-Name-B"
            );
        }

        @Override
        protected Object parse(String input) throws ParsingException {
            return Name.parse(input);
        }

        @Test
        @Override
        public void testEquals() throws ParsingException {
            super.testEquals();
            assertThat(Name.parse("Valid-Name")).isEqualTo(Name.parse("valid-name"));
        }
    }


    public static class ValueSpec extends ValjoSpec {

        public ValueSpec() {
            super(
                "valid value A",
                "valid value B"
            );
        }

        @Override
        protected Object parse(String input) throws ParsingException {
            return Value.parse(input);
        }

        @Test
        @Override
        public void parseEmptyInput() throws ParsingException {
            assertThat(Value.parse("")).hasToString("");
        }

        @Test
        @Override
        public void parseBlankInput() throws ParsingException {
            assertThat(Value.parse("  ")).hasToString("");
        }

        @Test
        public void parseInputWithLeadingWhitespace() throws ParsingException {
            assertThat(Value.parse(" valid input")).hasToString("valid input");
        }

        @Test
        public void parseInputWithTrailingWhitespace() throws ParsingException {
            assertThat(Value.parse("valid input ")).hasToString("valid input");
        }

        @Test
        public void parseInputWithInnerFoldingWhitespace() throws ParsingException {
            assertThat(Value.parse("valid\r\n input")).hasToString("valid input");
            assertThat(Value.parse("valid\r\n  input")).hasToString("valid input");
            assertThat(Value.parse("valid\r\n\t\tinput")).hasToString("valid input");
            assertThat(Value.parse("valid\r\n \r\n input")).hasToString("valid  input");
            assertThat(Value.parse("valid\r\n in\r\n put")).hasToString("valid in put");
        }

        @Test
        public void parseInputWithLeadingFoldingWhitespace() throws ParsingException {
            assertThat(Value.parse("\r\n valid input")).hasToString("valid input");
        }

        @Test
        public void parseInputWithMultipleFoldingWhitespace() throws ParsingException {
            assertThat(Value.parse("two\r\n valid lines")).hasToString("two valid lines");
            assertThat(Value.parse("three\r\n valid\r\n lines")).hasToString("three valid lines");
        }

        @Test
        public void parseInputWithTrailingFoldingWhitespace() throws ParsingException {
            assertThat(Value.parse("valid input\r\n ")).hasToString("valid input");
        }

        @Test
        public void parseInputWithNonAsciiCharacters() throws ParsingException {
            assertThat(Value.parse("Gültiger Eingabe")).hasToString("Gültiger Eingabe");
        }

        @Test
        public void parseIinputWithNonTabControlCharacter() {
            IntStream.range(0, 32)
                .filter(i -> i != '\t')
                .forEach(i -> assertInvalid(format("invalid%cinput", (char) i), (char) i, 7));
        }

        @Test
        public void parseInputWithNonTabControlCharacterImmediatelyFollowingFoldingWhitespace() {
            IntStream.range(0, 32)
                .filter(i -> i != '\t')
                .forEach(i -> assertInvalid(format("invalid\r\n %cinput", (char) i), (char) i, 10));
        }

        @Test
        public void parseInputWithCarriageReturnAtEOL() {
            assertInvalid("invalid input\r", '\r', 13);
        }

        @Test
        public void parseInputWithLinefeedAtEOL() {
            assertInvalid("invalid input\n", '\n', 13);
        }

        @Test
        public void parseInputWithCRLFAtEOL() {
            assertInvalid("invalid input\r\n", '\r', 13);
        }

        private void assertInvalid(String input, char invalidChar, int atIndex) {
            assertThatThrownBy(() -> Value.parse(input))
                .isInstanceOf(InvalidCharacterException.class);
        }
    }
}
