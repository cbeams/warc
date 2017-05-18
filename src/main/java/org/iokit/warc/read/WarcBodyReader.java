package org.iokit.warc.read;

import org.iokit.warc.WarcBody;
import org.iokit.warc.WarcHeader;

import org.iokit.core.read.ByteArrayReader;
import org.iokit.core.read.ParameterizedReader;
import org.iokit.core.read.ReaderException;

import java.io.InputStream;

public class WarcBodyReader implements ParameterizedReader<WarcHeader, WarcBody> {

    private final ByteArrayReader byteArrayReader;

    public WarcBodyReader(InputStream in) {
        this.byteArrayReader = new ByteArrayReader(in);
    }

    @Override
    public WarcBody read(WarcHeader header) throws ReaderException {
        return new WarcBody(byteArrayReader.read(header.getContentLength()));
    }
}
