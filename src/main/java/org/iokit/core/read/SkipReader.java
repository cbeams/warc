package org.iokit.core.read;

public class SkipReader extends TransformReader<InputReader, Void> {

    private final int count;

    public SkipReader(int count, InputReader reader) {
        super(reader);
        this.count = count;
    }

    @Override
    public Void read() throws ReaderException {
        for (int i=0; i < count; i++)
            reader.read();

        return null;
    }
}
