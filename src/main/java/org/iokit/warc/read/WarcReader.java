package org.iokit.warc.read;

import org.iokit.warc.WarcRecord;

import org.iokit.core.read.BoundedReader;
import org.iokit.core.read.LineReader;
import org.iokit.core.read.Reader;

import org.iokit.core.input.CrlfLineInputStream;
import org.iokit.core.input.LineInputStream;
import org.iokit.core.input.MagicInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class WarcReader extends BoundedReader<WarcRecord> {

    public static final int DEFAULT_MINIMUM_RECORD_COUNT = 1;

    public WarcReader(String warcFilePath) throws IOException {
        this(new File(warcFilePath));
    }

    public WarcReader(File warcFile) throws IOException {
        this(new FileInputStream(warcFile));
    }

    public WarcReader(InputStream in) throws IOException {
        this(new MagicInputStream(in));
    }

    public WarcReader(MagicInputStream in) {
        this(new CrlfLineInputStream(in));
    }

    public WarcReader(LineInputStream in) {
        this(in, new LineReader(in));
    }

    public WarcReader(LineInputStream in, LineReader lineReader) {
        this(in, new WarcRecordReader(in), new WarcSeparatorReader(lineReader));
    }

    public WarcReader(LineInputStream in, WarcRecordReader recordReader, Reader<?> concatenatorReader) {
        super(in, recordReader, concatenatorReader, DEFAULT_MINIMUM_RECORD_COUNT);
    }
}
