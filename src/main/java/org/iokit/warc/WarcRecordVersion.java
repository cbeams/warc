package org.iokit.warc;

import org.iokit.core.parse.NullInputException;
import org.iokit.core.parse.ParsingException;

import java.util.Objects;

public class WarcRecordVersion {

    public static final String WARC_1_0 = "WARC/1.0";
    public static final String WARC_1_1 = "WARC/1.1";

    private final String value;

    public WarcRecordVersion(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static WarcRecordVersion parse(String input) throws ParsingException {
        if (input == null)
            throw new NullInputException();

        if (!WARC_1_0.equals(input) && !WARC_1_1.equals(input))
            throw new ParsingException("[%s] is an unsupported or otherwise malformed WARC record version", input);

        return new WarcRecordVersion(input);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarcRecordVersion version = (WarcRecordVersion) o;
        return Objects.equals(value, version.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
