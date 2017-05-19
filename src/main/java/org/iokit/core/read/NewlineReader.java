package org.iokit.core.read;

import java.util.Optional;

public class NewlineReader extends TransformReader<LineReader, String> {

    public NewlineReader(LineReader reader) {
        super(reader);
    }

    @Override
    public Optional<String> readOptional() throws ReaderException {
        return reader.readOptional().filter(String::isEmpty);
    }
}
