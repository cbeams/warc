package org.iokit.core.read;

import java.util.Optional;

public class NewlineReader extends TransformReader<LineReader, String> implements OptionalReader<String> {

    public NewlineReader(LineReader reader) {
        super(reader);
    }

    public String read() throws ReaderException {
        return readOptional().orElseThrow(() -> new ReaderException("TODO"));
    }

    @Override
    public Optional<String> readOptional() throws ReaderException {
        return reader.readOptional().filter(line -> line.isEmpty());
    }
}
