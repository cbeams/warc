package org.iokit.warc.write;

import org.iokit.warc.WarcHeader;

import org.iokit.imf.write.FieldSetWriter;

import java.io.IOException;
import java.io.OutputStream;

public class WarcRecordHeaderWriter {

    private final OutputStream output;
    private final WarcRecordVersionWriter versionWriter;
    private final FieldSetWriter fieldSetWriter;

    public WarcRecordHeaderWriter(OutputStream output) {
        this(output, new WarcRecordVersionWriter(output), new FieldSetWriter(output));
    }

    public WarcRecordHeaderWriter(OutputStream output, WarcRecordVersionWriter versionWriter, FieldSetWriter fieldSetWriter) {
        this.output = output;
        this.versionWriter = versionWriter;
        this.fieldSetWriter = fieldSetWriter;
    }

    public void write(WarcHeader header) throws IOException {
        versionWriter.write(header.getVersion());
        output.write("\r\n".getBytes());
        fieldSetWriter.write(header.getFields());
        output.write("\r\n".getBytes());
    }
}
