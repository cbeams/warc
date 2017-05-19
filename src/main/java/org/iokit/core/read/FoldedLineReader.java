package org.iokit.core.read;

import org.iokit.core.input.LineInputStream;

import java.util.Optional;

public class FoldedLineReader extends LineReader {

    public FoldedLineReader(LineReader lineReader) {
        this(lineReader.getInput());
    }

    public FoldedLineReader(LineInputStream input) {
        super(input);
    }

    @Override
    public Optional<String> readOptional() throws ReaderException {
        Optional<String> firstLine = super.readOptional().filter(value -> !value.isEmpty());

        if (!firstLine.isPresent())
            return Optional.empty();

        StringBuilder lines = new StringBuilder(firstLine.get());

        while (isTabOrSpace(input.peek()))
            lines.append("\r\n").append(super.read());

        return Optional.of(lines.toString());
    }

    private boolean isTabOrSpace(byte b) {
        return b == ' ' || b == '\t';
    }
}
