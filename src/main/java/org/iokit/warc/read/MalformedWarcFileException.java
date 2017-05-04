package org.iokit.warc.read;

public class MalformedWarcFileException extends RuntimeException {

    public MalformedWarcFileException(String message) {
        super(message);
    }

    public MalformedWarcFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
