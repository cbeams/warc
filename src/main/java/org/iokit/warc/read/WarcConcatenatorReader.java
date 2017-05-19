package org.iokit.warc.read;

import org.iokit.core.read.ConcatenatorReader;
import org.iokit.core.read.SkipReader;
import org.iokit.core.read.LineReader;
import org.iokit.core.read.NewlineReader;

public class WarcConcatenatorReader extends ConcatenatorReader {

    public static final int NEWLINE_COUNT = 2;

    public WarcConcatenatorReader(LineReader lineReader) {
        super(new SkipReader(NEWLINE_COUNT, new NewlineReader(lineReader)));
    }
}
