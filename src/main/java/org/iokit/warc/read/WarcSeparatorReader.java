package org.iokit.warc.read;

import org.iokit.core.read.SkipReader;
import org.iokit.core.read.LineReader;
import org.iokit.core.read.NewlineReader;

public class WarcSeparatorReader extends SkipReader {

    public static final int NEWLINE_COUNT = 2;

    public WarcSeparatorReader(LineReader lineReader) {
        super(NEWLINE_COUNT, new NewlineReader(lineReader));
    }
}
