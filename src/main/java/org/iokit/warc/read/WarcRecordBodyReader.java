package org.iokit.warc.read;

import org.iokit.warc.WarcRecordBody;

import org.iokit.core.read.ByteArrayReader;
import org.iokit.core.read.FixedLengthReader;
import org.iokit.core.read.ReaderException;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.InputStream;

public class WarcRecordBodyReader implements FixedLengthReader<WarcRecordBody> {

    private final ByteArrayReader byteArrayReader;

    public WarcRecordBodyReader(String input) {
        this(new ByteArrayInputStream(input.getBytes()));
    }

    public WarcRecordBodyReader(InputStream in) {
        this.byteArrayReader = new ByteArrayReader(in);
    }

    @Override
    public WarcRecordBody read(int length) throws ReaderException, EOFException {
        return new WarcRecordBody(byteArrayReader.read(length));
    }
}
