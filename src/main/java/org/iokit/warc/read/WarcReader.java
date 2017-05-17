package org.iokit.warc.read;

import org.iokit.warc.WarcRecord;

import org.iokit.core.read.BoundedReader;
import org.iokit.core.read.LineReader;
import org.iokit.core.read.Reader;
import org.iokit.core.read.ReaderException;

import org.iokit.core.input.LineInputStream;

import java.util.zip.GZIPInputStream;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.UncheckedIOException;

import java.util.stream.Stream;

public class WarcReader extends BoundedReader<WarcRecord> {

    public static final int MIN_RECORD_COUNT = 1;

    public WarcReader(File warcFile) {
        this(plainOrGzipInputStream(warcFile));
    }

    public WarcReader(InputStream in) {
        this(new WarcInputStream(plainOrGzipInputStream(in)));
    }

    public WarcReader(LineInputStream in) {
        this(in, new LineReader(in));
    }

    public WarcReader(LineInputStream in, LineReader lineReader) {
        this(in, new WarcRecordReader(in), new WarcRecordSeparatorReader(lineReader));
    }

    public WarcReader(LineInputStream in, WarcRecordReader recordReader, Reader<Void> separatorReader) {
        super(in, recordReader, separatorReader, MIN_RECORD_COUNT);
    }

    @Override
    public WarcRecord read() {
        try {
            return super.read();
        } catch (EOFException ex) {
            throw new MalformedWarcFileException("Premature end of file", ex);
        } catch (ReaderException ex) {
            throw new MalformedWarcRecordException(ex);
        }
    }

    @Override
    public Stream<WarcRecord> stream() {
        return super.stream();
    }

    private static InputStream plainOrGzipInputStream(File file) {
        try {
            return plainOrGzipInputStream(new FileInputStream(file));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private static InputStream plainOrGzipInputStream(InputStream input) {
        if (input instanceof GZIPInputStream)
            return input;

        try {
            PushbackInputStream pb = new PushbackInputStream(input, 2);
            byte[] magic = new byte[2];
            int len = pb.read(magic);

            if (len < magic.length)
                return pb;

            pb.unread(magic, 0, len);

            if (magic[0] == (byte) 0x1f && magic[1] == (byte) 0x8b) // GZip magic number
                return new GZIPInputStream(pb);

            return pb;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
