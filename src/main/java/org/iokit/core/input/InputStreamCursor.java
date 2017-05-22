package org.iokit.core.input;

import java.io.InputStream;

public class InputStreamCursor implements Scrollable {

    public final boolean supported;

    private final InputStream in;

    public InputStreamCursor(InputStream in) {
        this.in = in;
        this.supported = in instanceof Scrollable;
    }

    @Override
    public boolean isAtEOF() {
        assertSupported();
        return ((Scrollable) in).isAtEOF();
    }

    @Override
    public void seek(long position) {
        assertSupported();
        ((Scrollable) in).seek(position);
    }

    private void assertSupported() {
        if (!supported)
            throw new UnsupportedOperationException();
    }
}
