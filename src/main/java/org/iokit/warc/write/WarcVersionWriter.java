package org.iokit.warc.write;

import org.iokit.warc.WarcVersion;

import org.iokit.core.write.LineWriter;
import org.iokit.core.write.Writer;

public class WarcVersionWriter extends Writer<WarcVersion> {

    private final LineWriter lineWriter;

    public WarcVersionWriter(LineWriter lineWriter) {
        super(lineWriter.getOutputStream());
        this.lineWriter = lineWriter;
    }

    public void write(WarcVersion version) {
        lineWriter.write(version.getValue());
    }
}
