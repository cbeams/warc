package org.iokit.message;

import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

public class Message<H extends Header, B extends Body> {

    private final H header;
    private final B body;

    public Message(H header, B body) {
        this.header = requireNonNull(header);
        this.body = requireNonNull(body);
    }

    public H getHeader() {
        return header;
    }

    public B getBody() {
        return body;
    }


    public static class Reader<H extends Header, B extends Body, M extends Message<H, B>>
        extends org.iokit.core.read.Reader<M> {

        protected final Header.Reader<H> headerReader;
        protected final Body.Reader<H, B> bodyReader;
        protected final BiFunction<H, B, M> messageFactory;

        public Reader(Header.Reader<H> headerReader, Body.Reader<H, B> bodyReader, BiFunction<H, B, M> messageFactory) {
            super(headerReader.in);
            this.headerReader = headerReader;
            this.bodyReader = bodyReader;
            this.messageFactory = messageFactory;
        }

        @Override
        public M read() {
            H header = headerReader.read();
            B body = bodyReader.read(header);
            return messageFactory.apply(header, body);
        }
    }


    public static class Writer<H extends Header, B extends Body, M extends Message<H, B>>
        extends org.iokit.core.write.Writer<M> {

        private final Header.Writer<H> headerWriter;
        private final Body.Writer<B> bodyWriter;

        public Writer(Header.Writer<H> headerWriter, Body.Writer<B> bodyWriter) {
            super(headerWriter.out);
            this.headerWriter = headerWriter;
            this.bodyWriter = bodyWriter;
        }

        @Override
        public void write(M message) {
            headerWriter.write(message.getHeader());
            bodyWriter.write(message.getBody());
        }
    }
}
