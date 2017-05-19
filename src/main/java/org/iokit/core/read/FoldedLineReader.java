package org.iokit.core.read;

import java.util.Optional;

public class FoldedLineReader extends OptionalReader<String> {

    private final LineReader lineReader;

    public FoldedLineReader(LineReader lineReader) {
        super(lineReader.getInput());
        this.lineReader = lineReader;
    }

    @Override
    public Optional<String> readOptional() throws ReaderException {
        Optional<String> firstLine = lineReader.readOptional().filter(value -> !value.isEmpty());

        if (!firstLine.isPresent())
            return Optional.empty();

        StringBuilder lines = new StringBuilder(firstLine.get());

        while (isTabOrSpace(lineReader.peek()))
            lines.append("\r\n").append(lineReader.read());

        return Optional.of(lines.toString());
    }

    private boolean isTabOrSpace(byte b) {
        return b == ' ' || b == '\t';
    }
}
