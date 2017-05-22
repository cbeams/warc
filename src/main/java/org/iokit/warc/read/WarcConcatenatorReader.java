package org.iokit.warc.read;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.NewlineReader;
import org.iokit.core.read.Reader;
import org.iokit.core.read.ReaderException;

public class WarcConcatenatorReader extends Reader<Boolean> {

    private final NewlineReader newlineReader;

    public WarcConcatenatorReader(LineReader lineReader) {
        this(new NewlineReader(lineReader));
    }

    public WarcConcatenatorReader(NewlineReader newlineReader) {
        super(newlineReader.in);
        this.newlineReader = newlineReader;
    }

    @Override
    public Boolean read() throws ReaderException {
        for (int i = 0; i < 2; i++) // TODO: extract constant for newline count
            if (!newlineReader.readOptional().isPresent())
                return false;

        return true;
    }
}
