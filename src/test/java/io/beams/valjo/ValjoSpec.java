package io.beams.valjo;

import org.iokit.core.parse.NullInputException;
import org.iokit.core.parse.ParsingException;

import org.junit.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public abstract class ValjoSpec {

    protected final String inputA;
    protected final String inputB;
    protected final Stream<String> validInputs;

    public ValjoSpec(String inputA, String inputB, String... moreInputs) {
        this.inputA = inputA;
        this.inputB = inputB;

        validInputs = Stream.concat(
            Stream.of(inputA, inputB),
            Stream.of(moreInputs));
    }

    protected abstract Object parse(String input) throws ParsingException;

    @Test
    public void validateInputs() throws ParsingException {
        // different strings must be supplied
        assertThat(inputA).isNotSameAs(inputB);

        // both must be valid, i.e. must parse without exception
        Object a = parse(inputA);
        Object b = parse(inputB);

        // and the resulting parsed objects must not be equivalent
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    public void parseValidInputs() throws ParsingException {
        validInputs.forEach(input ->
            assertThat(catchThrowable(() -> parse(input))).isNull());
    }

    @Test
    public void parseNullInput() throws ParsingException {
        assertThatThrownBy(() -> parse(null))
            .isInstanceOf(NullInputException.class);
    }

    @Test
    public void parseEmptyInput() throws ParsingException {
        assertThatThrownBy(() -> parse(""))
            .isInstanceOf(ParsingException.class);
    }

    @Test
    public void parseBlankInput() throws ParsingException {
        Stream.of(" ", "\t")
            .forEach(blank ->
                assertThatThrownBy(() -> parse(blank))
                    .isInstanceOf(ParsingException.class));
    }

    @Test
    public void testEquals() throws ParsingException {
        assertThat(parse(inputA)).isEqualTo(parse(inputA));
        assertThat(parse(inputA)).isNotEqualTo(parse(inputB));
    }

    @Test
    public void testHashCode() throws ParsingException {
        assertThat(parse(inputA).hashCode()).isEqualTo(parse(inputA).hashCode());
        assertThat(parse(inputA).hashCode()).isNotEqualTo(parse(inputB).hashCode());
    }

    @Test
    public void testToString() throws ParsingException {
        assertThat(parse(inputA)).hasToString(inputA);
        assertThat(parse(inputB)).hasToString(inputB);
    }
}
