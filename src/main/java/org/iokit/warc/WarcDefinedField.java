package org.iokit.warc;

import org.iokit.imf.DefinedField;
import org.iokit.imf.FieldName;

import java.util.function.Predicate;

public enum WarcDefinedField implements DefinedField { // TODO: support field-type specific validation
    WARC_Type(t -> true),
    WARC_Record_ID(t -> true),
    WARC_Date(t -> true),
    Content_Length(t -> true),
    Content_Type,
    WARC_Concurrent_To,
    WARC_Block_Digest,
    WARC_Payload_Digest,
    WARC_IP_Address,
    WARC_Refers_To,
    WARC_Target_URI,
    WARC_Truncated,
    WARC_Warcinfo_ID,
    WARC_Filename,
    WARC_Profile,
    WARC_Identified_Payload_Type,
    WARC_Segment_Origin_ID,
    WARC_Segment_Number,
    WARC_Segment_Total_Length;

    private final FieldName fieldName;
    private final Predicate<WarcType> required;

    WarcDefinedField() {
        this(t -> false);
    }

    WarcDefinedField(Predicate<WarcType> required) {
        this.fieldName = new FieldName(translate(name()));
        this.required = required;
    }

    private String translate(String name) {
        return name.replace('_', '-');
    }

    @Override
    public FieldName fieldName() {
        return fieldName;
    }

    public boolean isRequiredFor(WarcType type) {
        return required.test(type);
    }
}
