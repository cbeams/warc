package org.iokit.imf;

import org.iokit.core.read.ParameterizedReader;
import org.iokit.core.read.Reader;
import org.iokit.core.read.ReaderException;

import java.util.function.BiFunction;

public class MessageReader<H extends Header, B extends Body, M extends Message<H, B>> extends Reader<M> {

    protected final Reader<H> headerReader;
    protected final ParameterizedReader<H, B> bodyReader;
    protected final BiFunction<H, B, M> messageFactory;

    public MessageReader(Reader<H> headerReader, ParameterizedReader<H, B> bodyReader,
                         BiFunction<H, B, M> messageFactory) {
        super(headerReader.in);
        this.headerReader = headerReader;
        this.bodyReader = bodyReader;
        this.messageFactory = messageFactory;
    }

    @Override
    public M read() throws ReaderException {
        H header = headerReader.read();
        B body = bodyReader.read(header);
        return messageFactory.apply(header, body);
    }
}
