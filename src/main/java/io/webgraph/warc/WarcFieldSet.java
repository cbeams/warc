package io.webgraph.warc;

import org.iokit.message.Field;
import org.iokit.message.FieldNotFoundException;
import org.iokit.message.FieldNotPermittedException;
import org.iokit.message.FieldSet;
import org.iokit.message.FoldedLine;

import org.iokit.general.LineReader;
import org.iokit.general.LineWriter;

import java.util.Optional;
import java.util.function.Supplier;

import static io.webgraph.warc.WarcDefinedField.*;

public class WarcFieldSet extends FieldSet {

    public WarcType getType() {
        return getRequiredFieldValue(WARC_Type, WarcType::typeOf);
    }

    public String getRecordId() {
        return getRequiredFieldValue(WARC_Record_ID);
    }

    public String getDate() {
        return getRequiredFieldValue(WARC_Date);
    }

    public int getContentLength() {
        return getRequiredFieldValue(Content_Length, Integer::valueOf);
    }

    public Optional<String> getContentType() {
        return getFieldValue(Content_Type);
    }

    public Optional<String> getConcurrentTo() {
        return getFieldValue(WARC_Concurrent_To);
    }

    public Optional<String> getBlockDigest() {
        return getFieldValue(WARC_Block_Digest);
    }

    public Optional<String> getPayloadDigest() {
        return getFieldValue(WARC_Payload_Digest);
    }

    public Optional<String> getIpAddress() {
        return getFieldValue(WARC_IP_Address);
    }

    public Optional<String> getRefersTo() {
        return getFieldValue(WARC_Refers_To);
    }

    public Optional<String> getTargetUri() {
        return getFieldValue(WARC_Target_URI);
    }

    public Optional<String> getTruncated() {
        return getFieldValue(WARC_Truncated);
    }

    public Optional<String> getWarcinfoID() {
        return getFieldValue(WARC_Block_Digest);
    }

    public Optional<String> getFilename() {
        return getFieldValue(WARC_Filename);
    }

    public Optional<String> getProfile() {
        return getFieldValue(WARC_Profile);
    }

    public Optional<String> getIdentifiedPayloadType() {
        return getFieldValue(WARC_Identified_Payload_Type);
    }

    public Optional<String> getSegmentOriginID() {
        return getFieldValue(WARC_Segment_Origin_ID);
    }

    public Optional<String> getSegmentNumber() {
        return getFieldValue(WARC_Segment_Number);
    }

    public Optional<String> getSegmentTotalLength() {
        return getFieldValue(WARC_Segment_Total_Length);
    }


    public static class Reader extends FieldSet.Reader<WarcFieldSet> {

        public Reader(LineReader lineReader) {
            this(new FoldedLine.Reader(lineReader.in));
        }

        public Reader(FoldedLine.Reader lineReader) {
            this(new WarcField.Reader(lineReader), new WarcFieldSet.Validator());
        }

        public Reader(WarcField.Reader fieldReader, WarcFieldSet.Validator fieldSetValidator) {
            super(fieldReader, fieldSetValidator);
        }

        @Override
        protected Supplier<WarcFieldSet> newFieldSet() {
            return WarcFieldSet::new;
        }
    }


    public static class Writer extends FieldSet.Writer<WarcFieldSet> {

        private final LineWriter lineWriter;

        public Writer(LineWriter lineWriter) {
            super(new Field.Writer(lineWriter));
            this.lineWriter = lineWriter;
        }

        public void writeAfterFields() {
            lineWriter.write();
        }
    }


    public static class Validator extends FieldSet.Validator<WarcFieldSet> {

        @Override
        public void validate(WarcFieldSet fieldSet) {
            WarcType type = fieldSet.getType();

            for (WarcDefinedField definedField : WarcDefinedField.values())
                if (definedField.isMandatoryIn(type) && !fieldSet.getField(definedField).isPresent())
                    throw new FieldNotFoundException(definedField.fieldName());

            for (Field field : fieldSet)
                if (!WarcDefinedField.typeOf(field).isPermittedIn(type))
                    throw new FieldNotPermittedException(field, fieldSet);
        }
    }
}
