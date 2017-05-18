package org.iokit.warc.read;

import org.iokit.warc.WarcBody;
import org.iokit.warc.WarcHeader;
import org.iokit.warc.WarcRecord;

import org.iokit.imf.read.MessageReader;

import org.iokit.core.read.InputReader;
import org.iokit.core.read.LineReader;
import org.iokit.core.read.ParameterizedReader;

import org.iokit.core.input.Input;
import org.iokit.core.input.LineInputStream;

import java.util.function.BiFunction;

public class WarcRecordReader extends MessageReader<WarcHeader, WarcBody, WarcRecord> {

    public WarcRecordReader(LineInputStream in) {
        this(new LineReader(in));
    }

    public WarcRecordReader(LineReader lineReader) {
        this(new WarcHeaderReader(lineReader), new WarcBodyReader(lineReader.getInput()));
    }

    public WarcRecordReader(InputReader<Input, WarcHeader> headerReader,
                            ParameterizedReader<WarcHeader, WarcBody> bodyReader) {
        this(headerReader, bodyReader, WarcRecord::new);
    }

    public WarcRecordReader(InputReader<Input, WarcHeader> headerReader,
                            ParameterizedReader<WarcHeader, WarcBody> bodyReader,
                            BiFunction<WarcHeader, WarcBody, WarcRecord> recordFactory) {
        super(headerReader, bodyReader, recordFactory);
    }
}
