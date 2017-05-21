package org.iokit.warc.write;

import org.iokit.warc.WarcVersion;

import org.iokit.core.write.LineWriter;
import org.iokit.core.write.Writer;

import org.iokit.lang.Try;

public class WarcVersionWriter extends Writer<WarcVersion> {

    private final LineWriter lineWriter;

    public WarcVersionWriter(LineWriter lineWriter) {
        super(lineWriter.getOutput());
        this.lineWriter = lineWriter;
    }

    public void write(WarcVersion version) {
        Try.toRun(() -> lineWriter.write(version.getValue()));
    }
}
