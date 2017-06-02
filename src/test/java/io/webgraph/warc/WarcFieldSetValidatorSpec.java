package io.webgraph.warc;

import org.iokit.message.FieldNotFoundException;
import org.iokit.message.FieldNotPermittedException;

import org.iokit.general.LineReader;
import org.iokit.general.LineWriter;

import org.iokit.core.IOKitInputStream;
import org.iokit.core.IOKitOutputStream;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.util.List;
import java.util.stream.Stream;

import static io.webgraph.warc.WarcDefinedField.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThatCode;

public class WarcFieldSetValidatorSpec {

    WarcFieldSet.Validator validator = new WarcFieldSet.Validator();

    @Test
    public void validateMandatoryFieldsInMetadataFieldSet() {
        List<String> fieldSet = asList(
            "WARC-Type: metadata",
            "WARC-Record-ID: ...",
            "WARC-Date:      ...",
            "Content-Length: ...");

        assertThatCode(() -> validate(fieldSet)).doesNotThrowAnyException();

        Stream.of(WARC_Type, WARC_Record_ID, WARC_Date, Content_Length)
            .map(WarcDefinedField::displayName)
            .forEach(fieldName ->
                assertThatCode(() -> validate(excludeFrom(fieldSet, fieldName)))
                    .describedAs("Expected validation to fail because mandatory field '%s' is missing", fieldName)
                    .isInstanceOf(FieldNotFoundException.class)
                    .hasMessageContaining(fieldName));
    }

    @Test
    public void validateExtensionFieldsArePermitted() {
        List<String> fieldSet = asList(
            "WARC-Type: metadata",
            "WARC-Record-ID: ...",
            "WARC-Date:      ...",
            "Content-Length: ...",
            "X-My-Extension: ...");

        assertThatCode(() -> validate(fieldSet)).doesNotThrowAnyException();
    }

    @Test
    public void validateMandatoryFieldsInRevisitFieldSet() {
        List<String> fieldSet = asList(
            "WARC-Type:   revisit",
            "WARC-Record-ID:  ...",
            "WARC-Date:       ...",
            "Content-Length:  ...",
            "WARC-Target-URI: ...",
            "WARC-Profile:    ...");

        assertThatCode(() -> validate(fieldSet)).doesNotThrowAnyException();

        Stream.of(WARC_Profile)
            .map(WarcDefinedField::displayName)
            .forEach(fieldName ->
                assertThatCode(() -> validate(excludeFrom(fieldSet, fieldName)))
                    .describedAs("Expected validation to fail because mandatory field '%s' is missing", fieldName)
                    .isInstanceOf(FieldNotFoundException.class)
                    .hasMessageContaining(fieldName));
    }

    @Test
    public void validateMandatoryFieldsInContinuationFieldSet() {
        List<String> fieldSet = asList(
            "WARC-Type:     continuation",
            "WARC-Record-ID:         ...",
            "WARC-Date:              ...",
            "Content-Length:         ...",
            "WARC-Target-URI:        ...",
            "WARC-Segment-Number:    ...",
            "WARC-Segment-Origin-ID: ...");

        assertThatCode(() -> validate(fieldSet)).doesNotThrowAnyException();

        Stream.of(WARC_Segment_Origin_ID)
            .map(WarcDefinedField::displayName)
            .forEach(fieldName ->
                assertThatCode(() -> validate(excludeFrom(fieldSet, fieldName)))
                    .describedAs("Expected validation to fail because mandatory field '%s' is missing", fieldName)
                    .isInstanceOf(FieldNotFoundException.class)
                    .hasMessageContaining(fieldName));
    }

    @Test
    public void validateForbiddenFieldsInWarcinfoFieldSet() {
        List<String> fieldSet = asList(
            "WARC-Type:     warcinfo",
            "WARC-Record-ID:     ...",
            "WARC-Date: some     ...",
            "Content-Length:     ...",
            "WARC-Target-URI:    ...");

        String fieldName = WARC_Target_URI.displayName();

        assertThatCode(() -> validate(fieldSet))
            .describedAs("Expected validation to fail because field '%s' is forbidden", fieldName)
            .isInstanceOf(FieldNotPermittedException.class)
            .hasMessageContaining(fieldName);

        assertThatCode(() -> validate(excludeFrom(fieldSet, fieldName)))
            .doesNotThrowAnyException();
    }

    @Test
    public void validateLowercaseFieldsInWarcinfoFieldSet() {
        List<String> fieldSet = asList(
            "WARC-Type:     warcinfo",
            "WARC-Record-ID:     ...",
            "WARC-Date: some     ...",
            "Content-Length:     ...",
            "warc-target-uri:    ..."); // lowercase

        assertThatCode(() -> validate(fieldSet))
            .describedAs("Expected validation to fail because field '%s' is forbidden", "warc-target-uri")
            .isInstanceOf(FieldNotPermittedException.class)
            .hasMessageContaining("warc-target-uri");
    }

    void validate(List<String> lines) {
        validator.validate(fieldSetOf(lines));
    }

    WarcFieldSet fieldSetOf(List<String> lines) {
        return fieldSetReaderFor(inputOf(lines)).read();
    }

    WarcFieldSet.Reader fieldSetReaderFor(InputStream in) {
        return new WarcFieldSet.Reader(new LineReader(new IOKitInputStream(in)));
    }

    InputStream inputOf(List<String> lines) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LineWriter lineWriter = new LineWriter(new IOKitOutputStream(out));
        for (String line : lines)
            lineWriter.write(line);
        return new ByteArrayInputStream(out.toByteArray());
    }

    List<String> excludeFrom(List<String> fields, String fieldName) {
        return fields.stream()
            .filter(field -> !field.toLowerCase().startsWith(fieldName.toLowerCase()))
            .collect(toList());
    }
}
