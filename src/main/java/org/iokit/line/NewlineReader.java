package org.iokit.line;

import org.iokit.core.read.OptionalReader;

import org.iokit.core.validate.IOKitValidator;

import java.util.Optional;
import java.util.function.Predicate;

public class NewlineReader extends OptionalReader<String> {

    private final LineReader lineReader;

    public NewlineReader(LineReader lineReader) {
        super(lineReader.in);
        this.lineReader = lineReader;
    }

    @Override
    public Optional<String> readOptional() {
        return lineReader.readOptional().filter(assertNewline());
    }

    private static Predicate<String> assertNewline() {
        return string -> {
            if (!isNewline(string))
                throw new IOKitValidator.Exception("Expected an empty string (newline) but actually got [%s]", string);

            return true;
        };
    }

    public static boolean isNewline(String string) {
        return string.isEmpty();
    }
}
