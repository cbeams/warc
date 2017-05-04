package org.iokit.warc.read;

public class MalformedWarcRecordException extends RuntimeException {

    public MalformedWarcRecordException(Throwable cause) {
        super("see nested exception for details", cause);
    }
}
