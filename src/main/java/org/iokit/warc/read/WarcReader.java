package org.iokit.warc.read;

import org.iokit.warc.WarcRecord;

import org.iokit.core.read.ConcatenationReader;
import org.iokit.core.read.LineReader;
import org.iokit.core.read.Reader;

import org.iokit.core.input.LineInputStream;
import org.iokit.core.input.mapping.MappedInputStream;

import org.iokit.lang.Try;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class WarcReader extends ConcatenationReader<WarcRecord> {

    public static final int DEFAULT_MINIMUM_READ_COUNT = 1;

    public WarcReader(String warcFilePath) {
        this(new File(warcFilePath));
    }

    public WarcReader(File warcFile) {
        this(Try.toCall(() -> new FileInputStream(warcFile)));
    }

    public WarcReader(InputStream in) {
        this(new MappedInputStream(in));
    }

    public WarcReader(MappedInputStream in) {
        this(new LineInputStream(in, WarcRecordReader.DEFAULT_LINE_TERMINATOR));
    }

    public WarcReader(LineInputStream in) {
        this(new LineReader(in));
    }

    public WarcReader(LineReader lineReader) {
        this(new WarcRecordReader(lineReader), new WarcConcatenatorReader(lineReader));
    }

    public WarcReader(Reader<WarcRecord> recordReader, Reader<Boolean> concatenatorReader) {
        super(recordReader, concatenatorReader);
        setMinimumReadCount(DEFAULT_MINIMUM_READ_COUNT);
    }
}
