package org.iokit.warc.write;

import org.iokit.warc.WarcBody;

import java.io.IOException;
import java.io.OutputStream;

public class WarcRecordBodyWriter {

    private final OutputStream output;

    public WarcRecordBodyWriter(OutputStream output) {
        this.output = output;
    }

    public void write(WarcBody body) throws IOException {
        output.write(body.getData());
    }
}
