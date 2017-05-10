package org.iokit.warc.write;

import java.io.IOException;
import java.io.OutputStream;

public class WarcRecordSeparatorWriter {

    private final OutputStream output;

    public WarcRecordSeparatorWriter(OutputStream output) {
        this.output = output;
    }

    public void write() throws IOException {
        output.write("\r\n\r\n".getBytes());
    }
}
