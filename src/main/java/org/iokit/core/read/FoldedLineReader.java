package org.iokit.core.read;

public class FoldedLineReader implements Reader<String> {

    private final LineReader lineReader;

    public FoldedLineReader(LineReader lineReader) {
        this.lineReader = lineReader;
    }

    @Override
    public String read() throws ReaderException {
        String firstLine = lineReader.read();

        if (firstLine == null || firstLine.isEmpty())
            return null;

        StringBuilder lines = new StringBuilder(firstLine);

        while (isTabOrSpace(lineReader.peek()))
            lines.append("\r\n").append(lineReader.read());

        return lines.toString();
    }

    private boolean isTabOrSpace(byte b) {
        return b == ' ' || b == '\t';
    }
}
