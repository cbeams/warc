package org.iokit.imf.read;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.NewlineReader;
import org.iokit.core.read.ReaderException;

import org.iokit.core.input.LineInputStream;

import java.util.Optional;

import static org.iokit.core.LineTerminator.CR_LF;

public class FoldedLineReader extends LineReader { // TODO: make nested class of FoldedLine

    public FoldedLineReader(LineInputStream in) {
        super(in);
    }

    @Override
    public Optional<String> readOptional() throws ReaderException {
        Optional<String> firstLine = super.readOptional().filter(string -> !NewlineReader.isNewline(string));

        if (!firstLine.isPresent())
            return Optional.empty();

        StringBuilder lines = new StringBuilder(firstLine.get());

        while (nextLineStartsWithTabOrSpace())
            lines.append(CR_LF).append(super.read());

        return Optional.of(lines.toString());
    }

    private boolean nextLineStartsWithTabOrSpace() {
        return isTabOrSpace(in.peek());
    }

    private boolean isTabOrSpace(byte b) {
        return b == ' ' || b == '\t'; // TODO: Ascii
    }
}
