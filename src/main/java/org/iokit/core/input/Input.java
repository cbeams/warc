package org.iokit.core.input;

import java.io.Closeable;

public interface Input extends Closeable {

    boolean isComplete();

    void seek(long offset);
}
