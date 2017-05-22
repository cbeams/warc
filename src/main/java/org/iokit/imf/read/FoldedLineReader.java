package org.iokit.imf.read;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.NewlineReader;
import org.iokit.core.read.ReaderException;

import org.iokit.core.input.LineInputStream;

import org.iokit.core.LineTerminator;

import java.util.Optional;

public class FoldedLineReader extends LineReader {

    public static final LineTerminator DEFAULT_LINE_TERMINATOR = LineTerminator.CR_LF;

    private final LineTerminator terminator;

    public FoldedLineReader(LineInputStream in) {
        this(in, DEFAULT_LINE_TERMINATOR);
    }

    public FoldedLineReader(LineInputStream in, LineTerminator terminator) {
        super(in);
        this.terminator = terminator;
    }

    @Override
    public Optional<String> readOptional() throws ReaderException {
        Optional<String> firstLine = super.readOptional().filter(string -> !NewlineReader.isNewline(string));

        if (!firstLine.isPresent())
            return Optional.empty();

        StringBuilder lines = new StringBuilder(firstLine.get());

        while (nextLineStartsWithTabOrSpace())
            lines.append(terminator).append(super.read());

        return Optional.of(lines.toString());
    }

    private boolean nextLineStartsWithTabOrSpace() {
        return isTabOrSpace(in.peek());
    }

    private boolean isTabOrSpace(byte b) {
        return b == ' ' || b == '\t';
    }
}
