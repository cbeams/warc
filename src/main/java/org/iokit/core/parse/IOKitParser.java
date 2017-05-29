package org.iokit.core.parse;

public interface IOKitParser<V> {

    V parse(String input);
}
