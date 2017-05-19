package org.iokit.core.read;

import java.util.Optional;

public class NewlineReader extends Reader<String> {

    private final LineReader lineReader;

    public NewlineReader(LineReader lineReader) {
        super(lineReader.getInput());
        this.lineReader = lineReader;
    }

    @Override
    public Optional<String> readOptional() throws ReaderException {
        return lineReader.readOptional().filter(String::isEmpty);
    }
}
