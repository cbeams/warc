package io.webgraph.warc;

import org.iokit.general.LineReader;
import org.iokit.general.LineWriter;
import org.iokit.general.ValidatingParser;

import org.iokit.core.IOKitException;
import org.iokit.core.IOKitReader;
import org.iokit.core.IOKitValidator;
import org.iokit.core.IOKitWriter;

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


    public static class Reader extends IOKitReader<WarcVersion> {

        private final LineReader lineReader;
        private final WarcVersion.Parser parser;

        public Reader(LineReader lineReader) {
            this(lineReader, new WarcVersion.Parser());
        }

        public Reader(LineReader lineReader, WarcVersion.Parser parser) {
            super(lineReader.in);
            this.lineReader = lineReader;
            this.parser = parser;
        }

        public WarcVersion read() {
            return parser.parse(lineReader.read());
        }
    }


    public static class Writer extends IOKitWriter<WarcVersion> {

        private final LineWriter lineWriter;

        public Writer(LineWriter lineWriter) {
            super(lineWriter.out);
            this.lineWriter = lineWriter;
        }

        public void write(WarcVersion version) {
            lineWriter.write(version.getValue());
        }
    }


    public static class Parser extends ValidatingParser<WarcVersion> {

        public Parser() {
            this(new WarcVersion.Validator());
        }

        public Parser(IOKitValidator<String> validator) {
            super(validator);
        }

        @Override
        public WarcVersion parseValidated(String input) {
            return new WarcVersion(input);
        }
    }


    public static class Validator implements IOKitValidator<String> {

        @Override
        public void validate(String input) {
            if (!WARC_1_0.equals(input) && !WARC_1_1.equals(input)) {
                throw new IOKitException("[%s] is an unsupported or otherwise malformed WARC record version",
                    truncateIfNecessary(input));
            }
        }

        private String truncateIfNecessary(String input) {
            int len = input.length();
            int max = 8;

            return len <= max ?
                input :
                input.substring(0, max) + String.format("(+%d more characters)", len - max);
        }
    }
}
