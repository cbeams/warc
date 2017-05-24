package org.iokit.imf;

import org.iokit.line.LineInputStream;
import org.iokit.line.LineReader;
import org.iokit.line.NewlineReader;

import org.iokit.core.read.ReaderException;

import java.util.Optional;

import static org.iokit.core.Ascii.*;
import static org.iokit.line.LineTerminator.CR_LF;

public class FoldedLineReader extends LineReader {

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
        return b == SPACE || b == TAB;
    }
}
