package io.webgraph.warc;

import org.iokit.gzip.MultiMemberGzipInputStream;

import org.iokit.general.ConcatenationReader;
import org.iokit.general.LineReader;

import org.iokit.core.IOKitInputStream;
import org.iokit.core.Try;

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
        this(in, new MultiMemberGzipInputStream.Adapter());
    }

    public WarcReader(InputStream in, IOKitInputStream.Adapter... adapters) {
        this(IOKitInputStream.adapt(in, WarcRecord.LINE_TERMINATOR, adapters));
    }

    public WarcReader(IOKitInputStream in) {
        this(new LineReader(in));
    }

    public WarcReader(LineReader lineReader) {
        this(new WarcRecord.Reader(lineReader), new WarcConcatenator.Reader(lineReader));
    }

    public WarcReader(WarcRecord.Reader recordReader, WarcConcatenator.Reader concatenatorReader) {
        super(recordReader, concatenatorReader);
        setMinimumReadCount(DEFAULT_MINIMUM_READ_COUNT);
    }
}
