package org.iokit.warc;

import org.iokit.core.validate.ValidatorException;

import org.iokit.core.parse.ParsingException;
import org.iokit.core.parse.ValidatingParser;

import java.util.Objects;

public class WarcVersion {

    public static final String WARC_1_0 = "WARC/1.0";
    public static final String WARC_1_1 = "WARC/1.1";

    private final String value;

    public WarcVersion(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarcVersion version = (WarcVersion) o;
        return Objects.equals(value, version.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }


    public static class Parser extends ValidatingParser<WarcVersion> {

        public Parser() {
            this(new WarcVersion.Validator());
        }

        public Parser(org.iokit.core.validate.Validator<String> validator) {
            super(validator);
        }

        @Override
        public WarcVersion parseValidated(String input) throws ParsingException {
            return new WarcVersion(input);
        }
    }


    public static class Validator implements org.iokit.core.validate.Validator<String> {

        @Override
        public void validate(String input) throws ValidatorException {
            if (!WARC_1_0.equals(input) && !WARC_1_1.equals(input))
                throw new ValidatorException("[%s] is an unsupported or otherwise malformed WARC record version", input);
        }
    }
}
