package org.iokit.imf.read;

import org.iokit.imf.Field;

import org.iokit.core.read.LineReader;

import org.iokit.core.parse.ParsingException;

import org.iokit.core.input.LineInputStream;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;

import static org.assertj.core.api.Assertions.assertThat;

public class FieldReaderSpec {

    @Test
    public void test() throws ParsingException, EOFException {
        FieldReader fieldReader =
            new FieldReader(
                new LineReader(
                    new LineInputStream(
                        new ByteArrayInputStream("Field-Name: field value".getBytes()))));

        Field field = fieldReader.read();

        assertThat(field.getName()).hasToString("Field-Name");
        assertThat(field.getValue()).hasToString("field value");
    }
}
