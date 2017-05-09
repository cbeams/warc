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

        StringBuilder input = new StringBuilder(firstLine);

        while (true) {
            byte next = lineReader.peek();

            if (next != ' ' && next != '\t')
                break;

            input.append("\r\n").append(lineReader.read());
        }

        return input.toString();
    }
}
