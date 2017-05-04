package org.iokit.warc.read;

import org.iokit.core.read.CountOffReader;
import org.iokit.core.read.LineReader;
import org.iokit.core.read.NewlineReader;

public class WarcRecordSeparatorReader extends CountOffReader {

    public static final int NEWLINE_COUNT = 2;

    public WarcRecordSeparatorReader(LineReader lineReader) {
        super(NEWLINE_COUNT, new NewlineReader(lineReader));
    }
}
