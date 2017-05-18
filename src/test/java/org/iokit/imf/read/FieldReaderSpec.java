package org.iokit.imf.read;

import org.iokit.imf.Field;

import org.iokit.core.read.FoldedLineReader;
import org.iokit.core.read.LineReader;

import org.iokit.core.input.LineInputStream;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FieldReaderSpec {

    @Test
    public void readSimpleField() {
        FieldReader fieldReader =
            new FieldReader(
                new FoldedLineReader(
                    new LineReader(
                        new LineInputStream(
                            new ByteArrayInputStream("Field-Name: field value".getBytes())))));

        Field field = fieldReader.read();

        assertThat(field.getName()).hasToString("Field-Name");
        assertThat(field.getValue()).hasToString("field value");
    }

    @Test
    public void readFoldingWhitespace() {
        FieldReader fieldReader =
            new FieldReader(
                new FoldedLineReader(
                    new LineReader(
                        new LineInputStream(
                            new ByteArrayInputStream("Field-Name: field\r\n     value".getBytes())))));

        Field field = fieldReader.read();

        assertThat(field.getName()).hasToString("Field-Name");
        assertThat(field.getValue().getUnfoldedValue()).isEqualTo("field value");
        assertThat(field.getValue().getFoldedValue()).isEqualTo("field\r\n     value");
        assertThat(field.getValue()).hasToString("field value");
    }

    @Test
    public void readTwoLinesOfFoldingWhitespace() {
        FieldReader fieldReader =
            new FieldReader(
                new FoldedLineReader(
                    new LineReader(
                        new LineInputStream(
                            new ByteArrayInputStream("Field-Name: field\r\n     value\r\n cont".getBytes())))));

        Field field = fieldReader.read();

        assertThat(field.getName()).hasToString("Field-Name");
        assertThat(field.getValue()).hasToString("field value cont");
    }
}
