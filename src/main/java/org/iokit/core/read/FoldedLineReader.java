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

            int nextByte = lineReader.readByte();

            boolean folded = nextByte != -1 && (nextByte == ' ' || nextByte == '\t');

            lineReader.setPosition(lastPosition);

            if (!folded)
                break;

            String nextLine = lineReader.read();
            input.append("\r\n").append(nextLine);
        }

        return input.toString();
    }
}
