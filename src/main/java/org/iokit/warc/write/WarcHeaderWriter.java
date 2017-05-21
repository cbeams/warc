package org.iokit.warc.write;

import org.iokit.warc.WarcHeader;
import org.iokit.warc.WarcVersion;

import org.iokit.imf.write.FieldSetWriter;

import org.iokit.core.write.Writer;

import org.iokit.lang.Try;

import java.io.OutputStream;

public class WarcHeaderWriter extends Writer<WarcHeader> {

    private final OutputStream output;
    private final Writer<WarcVersion> versionWriter;
    private final FieldSetWriter fieldSetWriter;

    public WarcHeaderWriter(OutputStream output) {
        this(output, new WarcVersionWriter(output), new FieldSetWriter(output));
    }

    public WarcHeaderWriter(OutputStream output, Writer<WarcVersion> versionWriter, FieldSetWriter fieldSetWriter) {
        this.output = output;
        this.versionWriter = versionWriter;
        this.fieldSetWriter = fieldSetWriter;
    }

    @Override
    public void write(WarcHeader header) {
        versionWriter.write(header.getVersion());
        Try.toRun(() -> output.write("\r\n".getBytes()));
        fieldSetWriter.write(header.getFields());
        Try.toRun(() -> output.write("\r\n".getBytes()));
    }
}
