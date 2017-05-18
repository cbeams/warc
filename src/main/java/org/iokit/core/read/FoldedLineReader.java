package org.iokit.core.read;

public class FoldedLineReader extends TransformReader<LineReader, String> {

    public FoldedLineReader(LineReader reader) {
        super(reader);
    }

    @Override
    public String read() throws ReaderException {
        String firstLine = reader.read();

        if (firstLine == null || firstLine.isEmpty())
            return null;

        StringBuilder lines = new StringBuilder(firstLine);

        while (isTabOrSpace(reader.peek()))
            lines.append("\r\n").append(reader.read());

        return lines.toString();
    }

    private boolean isTabOrSpace(byte b) {
        return b == ' ' || b == '\t';
    }
}
