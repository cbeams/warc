package org.iokit.core.read;

public class SkipReader extends TransformReader<Reader, Boolean> {

    private final int times;

    public SkipReader(int times, Reader reader) {
        super(reader);
        this.times = times;
    }

    @Override
    public Boolean read() throws ReaderException {
        for (int i = 0; i < times; i++)
            if (!reader.readOptional().isPresent())
                return false;

        return true;
    }
}
