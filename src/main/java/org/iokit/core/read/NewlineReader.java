package org.iokit.core.read;

import java.util.Optional;

public class NewlineReader extends OptionalReader<String> {

    private final LineReader lineReader;

    public NewlineReader(LineReader lineReader) {
        super(lineReader.getInputStream());
        this.lineReader = lineReader;
    }

    @Override
    public Optional<String> readOptional() throws ReaderException {
        return lineReader.readOptional().filter(String::isEmpty);
    }
}
