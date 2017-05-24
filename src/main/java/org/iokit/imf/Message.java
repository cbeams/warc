package org.iokit.imf;

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
}
