package org.iokit.imf;

import org.iokit.core.write.Writer;

public class MessageWriter<H extends Header, B extends Body, M extends Message<H, B>> extends Writer<M> {

    private final org.iokit.core.write.Writer<H> headerWriter;
    private final org.iokit.core.write.Writer<B> bodyWriter;

    public MessageWriter(org.iokit.core.write.Writer<H> headerWriter,
                         org.iokit.core.write.Writer<B> bodyWriter) {
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
