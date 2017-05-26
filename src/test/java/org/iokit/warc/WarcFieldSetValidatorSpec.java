package org.iokit.warc;

import org.iokit.message.FieldNotFoundException;

import org.iokit.line.LineInputStream;
import org.iokit.line.LineReader;
import org.iokit.line.LineTerminator;
import org.iokit.line.LineWriter;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.iokit.warc.WarcDefinedField.*;

public class WarcFieldSetValidatorSpec {

    WarcFieldSet.Validator validator = new WarcFieldSet.Validator();

    List<String> validFieldSet = asList(
        "WARC-Type: metadata",
        "WARC-Record-ID: someval",
        "WARC-Date: some date",
        "Content-Length: 42");


    @Test
    public void validateFieldSetWithAllMandatoryFields() {
        assertThatCode(() -> validate(validFieldSet)).doesNotThrowAnyException();
    }

    @Test
    public void validateFieldSetMissingOneMandatoryField() {
        Stream.of(WARC_Type, WARC_Record_ID, WARC_Date, Content_Length) // mandatory fields
            .map(WarcDefinedField::displayName)
            .forEach(fieldName ->
                assertThatCode(() -> validate(excludeFrom(validFieldSet, fieldName)))
                    .describedAs("Expected validation to fail because mandatory field '%s' is missing", fieldName)
                    .isInstanceOf(FieldNotFoundException.class)
                    .hasMessageContaining(fieldName));
    }


    void validate(List<String> lines) {
        validator.validate(fieldSetOf(lines));
    }

    WarcFieldSet fieldSetOf(List<String> lines) {
        return fieldSetReaderFor(inputOf(lines)).read();
    }

    WarcFieldSet.Reader fieldSetReaderFor(InputStream in) {
        return new WarcFieldSet.Reader(
            new LineReader(
                new LineInputStream(in, LineTerminator.CR_LF)));
    }

    InputStream inputOf(List<String> lines) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LineWriter lineWriter = new LineWriter(out, LineTerminator.CR_LF);
        for (String line : lines)
            lineWriter.write(line);
        return new ByteArrayInputStream(out.toByteArray());
    }

    List<String> excludeFrom(List<String> fields, String fieldName) {
        return fields.stream()
            .filter(field -> !field.startsWith(fieldName))
            .collect(toList());
    }
}
