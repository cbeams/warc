package org.iokit.message;

import org.iokit.core.validate.InvalidCharacterException;

import io.beams.valjo.ValjoSpec;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    FieldSpecs.FieldSpec.class,
    FieldSpecs.NameSpec.class,
    FieldSpecs.ValueSpec.class
})
public class FieldSpecs {

    public static class FieldSpec extends ValjoSpec {

        public FieldSpec() {
            this(
                "Valid-Name: valid value A",
                "Valid-Name: valid value B"
            );
        }

        protected FieldSpec(String inputA, String inputB, String... moreInputs) {
            super(inputA, inputB, moreInputs);
        }

        @Override
        protected Object parse(String input) {
            return new Field.Parser().parse(input);
        }

        @Test
        public void parseSplitsNameAndValueOnSeparator() {
            Field field = new Field.Parser().parse("Valid-Name: valid value");
            assertThat(field.getName()).hasToString("Valid-Name");
            assertThat(field.getValue()).hasToString("valid value");
        }

        @Test
        public void parseInputWithoutFieldSeparator() {
            assertThatThrownBy(() -> new Field.Parser().parse("invalid input"))
                .isInstanceOf(Field.Parser.MissingSeparatorException.class);
        }

        @Override
        @Test
        public void parseEmptyInput() {
            assertThatThrownBy(() -> parse(""))
                .isInstanceOf(Field.Parser.MissingSeparatorException.class);
        }

        @Override
        @Test
        public void parseBlankInput() {
            Stream.of(" ", "\t")
                .forEach(blank ->
                    assertThatThrownBy(() -> parse(blank))
                        .isInstanceOf(Field.Parser.MissingSeparatorException.class));
        }
    }


    public static class NameSpec extends TokenSpec {

        private org.iokit.core.parse.Parser parser = new FieldName.Parser();

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
            return new FieldValue.Parser().parse(input);
        }

        @Test
        @Override
        public void parseEmptyInput() {
            assertThat(new FieldValue.Parser().parse("")).hasToString("");
        }

        @Test
        @Override
        public void parseBlankInput() {
            assertThat(new FieldValue.Parser().parse("  ")).hasToString("");
        }

        @Test
        public void parseInputWithLeadingWhitespace() {
            assertThat(new FieldValue.Parser().parse(" valid input")).hasToString("valid input");
        }

        @Test
        public void parseInputWithTrailingWhitespace() {
            assertThat(new FieldValue.Parser().parse("valid input ")).hasToString("valid input");
        }

        @Test
        public void parseInputWithInnerFoldingWhitespace() {
            assertThat(new FieldValue.Parser().parse("valid\r\n input")).hasToString("valid input");
            assertThat(new FieldValue.Parser().parse("valid\r\n  input")).hasToString("valid input");
            assertThat(new FieldValue.Parser().parse("valid\r\n\t\tinput")).hasToString("valid input");
            assertThat(new FieldValue.Parser().parse("valid\r\n \r\n input")).hasToString("valid  input");
            assertThat(new FieldValue.Parser().parse("valid\r\n in\r\n put")).hasToString("valid in put");
        }

        @Test
        public void parseInputWithLeadingFoldingWhitespace() {
            assertThat(new FieldValue.Parser().parse("\r\n valid input")).hasToString("valid input");
        }

        @Test
        public void parseInputWithMultipleFoldingWhitespace() {
            assertThat(new FieldValue.Parser().parse("two\r\n valid lines")).hasToString("two valid lines");
            assertThat(new FieldValue.Parser().parse("three\r\n valid\r\n lines")).hasToString("three valid lines");
        }

        @Test
        public void parseInputWithTrailingFoldingWhitespace() {
            assertThat(new FieldValue.Parser().parse("valid input\r\n ")).hasToString("valid input");
        }

        @Test
        public void parseInputWithNonAsciiCharacters() {
            assertThat(new FieldValue.Parser().parse("Gültiger Eingabe")).hasToString("Gültiger Eingabe");
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
            assertThatThrownBy(() -> new FieldValue.Parser().parse(input))
                .isInstanceOf(InvalidCharacterException.class);
        }
    }
}
