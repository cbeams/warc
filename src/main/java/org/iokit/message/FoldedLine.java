package org.iokit.message;

import org.iokit.line.LineInputStream;
import org.iokit.line.LineReader;
import org.iokit.line.NewlineReader;

import org.iokit.core.validate.InvalidCharacterException;

import java.util.Optional;

import static org.iokit.coding.Ascii.*;
import static org.iokit.line.LineTerminator.CR_LF;

public class FoldedLine {


    public static class Reader extends LineReader {

        public Reader(LineInputStream in) {
            super(in);
        }

        @Override
        public Optional<String> readOptional() {
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


    public static class Validator implements org.iokit.core.validate.Validator<String> {

        @Override
        public void validate(String input) {

            char[] chars = input.toCharArray();
            for (int index = 0, increment = 1; index < chars.length; index += increment, increment = 1) {
                char c = chars[index];

                if (c == TAB)
                    continue;

                if (c == CR_LF.bytes[0]
                    && chars.length > index + CR_LF.bytes.length
                    && chars[index + 1] == CR_LF.bytes[1]
                    && (chars[index + 2] == SPACE || chars[index + 2] == TAB)) {

                    increment = 3;
                    continue;
                }

                if (isAsciiControlChar(c))
                    throw new InvalidCharacterException(input, c, index);
            }
        }
    }
}
