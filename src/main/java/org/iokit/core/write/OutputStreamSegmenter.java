package org.iokit.core.write;

import java.io.OutputStream;

public class OutputStreamSegmenter implements Segmentable {

    public final boolean supported;

    private final OutputStream out;

    public OutputStreamSegmenter(OutputStream out) {
        this.out = out;
        this.supported = out instanceof Segmentable;
    }

    @Override
    public void startSegment() {
        assertSupported();
        ((Segmentable) out).startSegment();
    }

    @Override
    public void finishSegment() {
        assertSupported();
        ((Segmentable) out).finishSegment();
    }

    private void assertSupported() {
        if (!supported)
            throw new UnsupportedOperationException();
    }
}
