package org.iokit.core.read;

import java.io.EOFException;

public class FoldedLineReader implements Reader<String> {

    private final LineReader lineReader;

    public FoldedLineReader(LineReader lineReader) {
        this.lineReader = lineReader;
    }

    @Override
    public String read() throws ReaderException, EOFException {
        String firstLine = lineReader.read();
        if (firstLine == null || firstLine.isEmpty())
            return null;

        StringBuilder input = new StringBuilder();
        input.append(firstLine);

        while (true) {
            long lastPosition = lineReader.getPosition();
            try {
                String next = lineReader.read();
                if (next != null && !next.isEmpty() && (next.charAt(0) == ' ' || next.charAt(0) == '\t')) {
                    input.append("\r\n").append(next);
                    continue;
                }
            } catch (EOFException ignore) {
            }
            lineReader.setPosition(lastPosition);
            break;
        }

        return input.toString();
    }
}
