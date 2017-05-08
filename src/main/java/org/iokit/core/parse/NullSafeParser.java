package org.iokit.core.parse;

public abstract class NullSafeParser<T> implements Parser<T> {

    public T parse(String input) throws ParsingException {
        if (input == null)
            throw new NullInputException();

        return parseNullSafe(input);
    }

    public abstract T parseNullSafe(String input) throws ParsingException;
}
