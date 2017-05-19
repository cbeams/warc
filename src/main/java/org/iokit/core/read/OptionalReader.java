package org.iokit.core.read;

import java.util.Optional;

public interface OptionalReader<T> extends Reader<T> {

    Optional<T> readOptional() throws ReaderException;
}
