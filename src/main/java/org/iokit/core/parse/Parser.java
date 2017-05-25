package org.iokit.core.parse;

public interface Parser<V> {

    V parse(String input);
}
