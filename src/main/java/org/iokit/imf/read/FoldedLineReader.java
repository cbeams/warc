package org.iokit.imf.read;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.ReaderException;

import org.iokit.core.input.LineInputStream;

import java.util.Optional;

public class FoldedLineReader extends LineReader {

    public FoldedLineReader(LineInputStream in) {
        super(in);
    }

    @Override
    public Optional<String> readOptional() throws ReaderException {
        Optional<String> firstLine = super.readOptional().filter(value -> !value.isEmpty());

        if (!firstLine.isPresent())
            return Optional.empty();

        StringBuilder lines = new StringBuilder(firstLine.get());

        while (isTabOrSpace(in.peek()))
            lines.append("\r\n").append(super.read());

        return Optional.of(lines.toString());
    }

    private boolean isTabOrSpace(byte b) {
        return b == ' ' || b == '\t';
    }
}
