package org.iokit.imf;

import org.iokit.imf.parse.FieldNameParser;
import org.iokit.imf.parse.FieldParser;
import org.iokit.imf.parse.FieldValueParser;

import org.iokit.core.validate.InvalidCharacterException;

import org.iokit.core.parse.Parser;
import org.iokit.core.parse.TokenSpec;

import io.beams.valjo.ValjoSpec;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.iokit.imf.Field.Name;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    FieldSpecs.FieldSpec.class,
    FieldSpecs.NameSpec.class,
    FieldSpecs.ValueSpec.class
})
public class FieldSpecs {

    public static class FieldSpec extends ValjoSpec {

        public FieldSpec() {
            super(
                "Valid-Name: valid value A",
                "Valid-Name: valid value B"
            );
        }

        @Override
        protected Object parse(String input) {
            return new FieldParser().parse(input);
        }

        @Test
        public void parseSplitsNameAndValueOnSeparator() {
            Field field = new FieldParser().parse("Valid-Name: valid value");
            assertThat(field.getName()).hasToString("Valid-Name");
            assertThat(field.getValue()).hasToString("valid value");
        }

        @Test
        public void parseInputWithoutFieldSeparator() {
            assertThatThrownBy(() -> new FieldParser().parse("invalid input"))
                .isInstanceOf(FieldParser.MissingSeparatorException.class);
        }

        @Override
        @Test
        public void parseEmptyInput() {
            assertThatThrownBy(() -> parse(""))
                .isInstanceOf(FieldParser.MissingSeparatorException.class);
        }

        @Override
        @Test
        public void parseBlankInput() {
            Stream.of(" ", "\t")
                .forEach(blank ->
                    assertThatThrownBy(() -> parse(blank))
                        .isInstanceOf(FieldParser.MissingSeparatorException.class));
        }
    }


    public static class NameSpec extends TokenSpec {

        private Parser<Name> parser = new FieldNameParser();

        public NameSpec() {
            super(
                "Valid-Name-A",
                "Valid-Name-B"
            );
        }

        @Override
        protected Object parse(String input) {
            return parser.parse(input);
        }

        @Test
        @Override
        public void testEquals() {
            super.testEquals();
            assertThat(parse("Valid-Name")).isEqualTo(parse("valid-name"));
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
        protected Object parse(String input) {
            return new FieldValueParser().parse(input);
        }

        @Test
        @Override
        public void parseEmptyInput() {
            assertThat(new FieldValueParser().parse("")).hasToString("");
        }

        @Test
        @Override
        public void parseBlankInput() {
            assertThat(new FieldValueParser().parse("  ")).hasToString("");
        }

        @Test
        public void parseInputWithLeadingWhitespace() {
            assertThat(new FieldValueParser().parse(" valid input")).hasToString("valid input");
        }

        @Test
        public void parseInputWithTrailingWhitespace() {
            assertThat(new FieldValueParser().parse("valid input ")).hasToString("valid input");
        }

        @Test
        public void parseInputWithInnerFoldingWhitespace() {
            assertThat(new FieldValueParser().parse("valid\r\n input")).hasToString("valid input");
            assertThat(new FieldValueParser().parse("valid\r\n  input")).hasToString("valid input");
            assertThat(new FieldValueParser().parse("valid\r\n\t\tinput")).hasToString("valid input");
            assertThat(new FieldValueParser().parse("valid\r\n \r\n input")).hasToString("valid  input");
            assertThat(new FieldValueParser().parse("valid\r\n in\r\n put")).hasToString("valid in put");
        }

        @Test
        public void parseInputWithLeadingFoldingWhitespace() {
            assertThat(new FieldValueParser().parse("\r\n valid input")).hasToString("valid input");
        }

        @Test
        public void parseInputWithMultipleFoldingWhitespace() {
            assertThat(new FieldValueParser().parse("two\r\n valid lines")).hasToString("two valid lines");
            assertThat(new FieldValueParser().parse("three\r\n valid\r\n lines")).hasToString("three valid lines");
        }

        @Test
        public void parseInputWithTrailingFoldingWhitespace() {
            assertThat(new FieldValueParser().parse("valid input\r\n ")).hasToString("valid input");
        }

        @Test
        public void parseInputWithNonAsciiCharacters() {
            assertThat(new FieldValueParser().parse("Gültiger Eingabe")).hasToString("Gültiger Eingabe");
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
            assertThatThrownBy(() -> new FieldValueParser().parse(input))
                .isInstanceOf(InvalidCharacterException.class);
        }
    }
}
