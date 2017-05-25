package org.iokit.core.parse;

public interface Parser<T> {

    T parse(String input);
}
