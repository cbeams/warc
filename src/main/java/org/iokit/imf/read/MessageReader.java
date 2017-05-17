package org.iokit.imf.read;

import org.iokit.imf.Message;
import org.iokit.imf.Body;
import org.iokit.imf.Header;

import org.iokit.core.read.ParameterizedReader;
import org.iokit.core.read.Reader;
import org.iokit.core.read.ReaderException;

import java.io.EOFException;

import java.util.function.BiFunction;

public class MessageReader<H extends Header, B extends Body, M extends Message> implements Reader<M> {

    protected final Reader<H> headerReader;
    protected final ParameterizedReader<H, B> bodyReader;
    protected final BiFunction<H, B, M> messageFactory;

    public MessageReader(Reader<H> headerReader, ParameterizedReader<H, B> bodyReader,
                         BiFunction<H, B, M> messageFactory) {
        this.headerReader = headerReader;
        this.bodyReader = bodyReader;
        this.messageFactory = messageFactory;
    }

    @Override
    public M read() throws ReaderException, EOFException {
        H header = headerReader.read();
        B body = bodyReader.read(header);
        return messageFactory.apply(header, body);
    }
}
