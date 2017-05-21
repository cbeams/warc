package org.iokit.warc.write;

import org.iokit.warc.WarcHeader;
import org.iokit.warc.WarcVersion;

import org.iokit.imf.write.FieldSetWriter;

import org.iokit.core.write.LineWriter;
import org.iokit.core.write.Writer;

public class WarcHeaderWriter extends Writer<WarcHeader> {

    private final Writer<WarcVersion> versionWriter;
    private final FieldSetWriter fieldSetWriter;
    private final LineWriter lineWriter;

    public WarcHeaderWriter(LineWriter lineWriter) {
        this(new WarcVersionWriter(lineWriter), new FieldSetWriter(lineWriter), lineWriter);
    }

    public WarcHeaderWriter(Writer<WarcVersion> versionWriter, FieldSetWriter fieldSetWriter, LineWriter lineWriter) {
        super(versionWriter.getOutput());
        this.versionWriter = versionWriter;
        this.fieldSetWriter = fieldSetWriter;
        this.lineWriter = lineWriter;
    }

    @Override
    public void write(WarcHeader header) {
        versionWriter.write(header.getVersion());
        fieldSetWriter.write(header.getFields());
        lineWriter.writeNewLine();
    }
}
