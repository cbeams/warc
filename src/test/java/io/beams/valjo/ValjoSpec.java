package io.beams.valjo;

import org.iokit.core.validate.InvalidCharacterException;
import org.iokit.core.validate.InvalidLengthException;

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

    protected abstract Object parse(String input);

    @Test
    public void validateInputs() {
        // different strings must be supplied
        assertThat(inputA).isNotSameAs(inputB);

        // both must be valid, i.e. must parse without exception
        Object a = parse(inputA);
        Object b = parse(inputB);

        // and the resulting parsed objects must not be equivalent
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    public void parseValidInputs() {
        validInputs.forEach(input ->
            assertThat(catchThrowable(() -> parse(input))).isNull());
    }

    @Test
    public void parseNullInput() {
        assertThatThrownBy(() -> parse(null))
            .isInstanceOf(Exception.class);
    }

    @Test
    public void parseEmptyInput() {
        assertThatThrownBy(() -> parse(""))
            .isInstanceOf(InvalidLengthException.class);
    }

    @Test
    public void parseBlankInput() {
        Stream.of(" ", "\t")
            .forEach(blank ->
                assertThatThrownBy(() -> parse(blank))
                    .isInstanceOf(InvalidCharacterException.class));
    }

    @Test
    public void testEquals() {
        assertThat(parse(inputA)).isEqualTo(parse(inputA));
        assertThat(parse(inputA)).isNotEqualTo(parse(inputB));
    }

    @Test
    public void testHashCode() {
        assertThat(parse(inputA).hashCode()).isEqualTo(parse(inputA).hashCode());
        assertThat(parse(inputA).hashCode()).isNotEqualTo(parse(inputB).hashCode());
    }

    @Test
    public void testToString() {
        assertThat(parse(inputA)).hasToString(inputA);
        assertThat(parse(inputB)).hasToString(inputB);
    }
}
