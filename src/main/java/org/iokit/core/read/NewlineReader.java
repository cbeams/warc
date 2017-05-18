package org.iokit.core.read;

public class NewlineReader extends TransformReader<LineReader, Void> {

    public NewlineReader(LineReader reader) {
        super(reader);
    }

    public Void read() throws ReaderException {
        String value = reader.read();

        if (!value.isEmpty())
            throw new ReaderException("expected to read a newline but got [%s]", value);

        return null;
    }
}
