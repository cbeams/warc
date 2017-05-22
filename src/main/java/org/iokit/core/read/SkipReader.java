package org.iokit.core.read;

public class SkipReader extends Reader<Boolean> {

    private final int times;
    private final OptionalReader reader;

    public SkipReader(int times, OptionalReader reader) {
        super(reader.getInputStream());
        this.times = times;
        this.reader = reader;
    }

    @Override
    public Boolean read() throws ReaderException {
        for (int i = 0; i < times; i++)
            if (!reader.readOptional().isPresent())
                return false;

        return true;
    }
}
