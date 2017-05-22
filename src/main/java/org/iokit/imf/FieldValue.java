package org.iokit.imf;

import org.iokit.core.validate.Validator;

import org.iokit.core.parse.ParsingException;
import org.iokit.core.parse.ValidatingParser;

import java.util.Objects;
import java.util.regex.Pattern;

public class FieldValue {

    private final Pattern FOLDED_WHITESPACE_REGEX = Pattern.compile("\\r\\n[ \\t]+"); // TODO: format

    private final String value;

    public FieldValue(String value) {
        this.value = value;
    }

    public String getFoldedValue() {
        return value;
    }

    public String getUnfoldedValue() {
        return FOLDED_WHITESPACE_REGEX.matcher(value).replaceAll(" "); // TODO: extract to Ascii.SPACE
    }

    @Override
    public String toString() {
        return getUnfoldedValue();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        FieldValue value = (FieldValue) that;
        return Objects.equals(this.value, value.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }


    public static class Parser extends ValidatingParser<FieldValue> {

        public Parser() {
            this(new FoldedLineValidator());
        }

        public Parser(Validator<String> validator) {
            super(validator);
        }


        public FieldValue parseValidated(String input) throws ParsingException {
            return new FieldValue(input.trim());
        }
    }
}
