package org.iokit.core.input;

import org.iokit.core.token.LineTerminator;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class CompletionAwareLineInputStream extends LineInputStream implements CompletionAwareInput {

    public CompletionAwareLineInputStream(InputStream in, LineTerminator... terminators) {
        super(in, terminators);
    }

    @Override
    public boolean isComplete() {
        try {
            long pos = in.position();
            try {
                return in.read() == -1;
            } finally {
                in.position(pos);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
