package org.iokit.core.read;

import org.iokit.core.validate.ValidatorException;

import java.util.Optional;
import java.util.function.Predicate;

public class NewlineReader extends OptionalReader<String> {

    private final LineReader lineReader;

    public NewlineReader(LineReader lineReader) {
        super(lineReader.in);
        this.lineReader = lineReader;
    }

    @Override
    public Optional<String> readOptional() throws ReaderException {
        return lineReader.readOptional().filter(validNewline());
    }

    private Predicate<String> validNewline() {
        return string -> {
            if (!string.isEmpty())
                throw new ValidatorException("Expected an empty string (newline) but actually got [" + string + "]");

            return true;
        };
    }
}
