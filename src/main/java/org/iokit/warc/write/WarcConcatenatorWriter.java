package org.iokit.warc.write;

import org.iokit.core.write.Writer;

import org.iokit.lang.Try;

import java.io.OutputStream;

public class WarcConcatenatorWriter extends Writer<Void> {

    private final OutputStream output;

    public WarcConcatenatorWriter(OutputStream output) {
        this.output = output;
    }

    public void write(Void value) {
        Try.toRun(() -> output.write("\r\n\r\n".getBytes()));
    }
}
